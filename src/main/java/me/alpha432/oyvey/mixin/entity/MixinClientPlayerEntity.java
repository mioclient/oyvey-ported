package me.alpha432.oyvey.mixin.entity;

import me.alpha432.oyvey.event.Stage;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import me.alpha432.oyvey.event.impl.entity.player.MoveEvent;
import me.alpha432.oyvey.event.impl.entity.player.TickEvent;
import me.alpha432.oyvey.event.impl.entity.player.UpdateWalkingPlayerEvent;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static me.alpha432.oyvey.util.traits.Util.EVENT_BUS;

@Mixin(LocalPlayer.class)
public class MixinClientPlayerEntity {
    @Inject(method = "tick", at = @At("HEAD"))
    private void tickPreHook(CallbackInfo ci) {
        EVENT_BUS.post(new TickEvent.Pre());
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void tickHook(CallbackInfo ci) {
        EVENT_BUS.post(new TickEvent.Post());
    }

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/AbstractClientPlayer;tick()V", shift = At.Shift.AFTER))
    private void tickHook2(CallbackInfo ci) {
        EVENT_BUS.post(new UpdateWalkingPlayerEvent(Stage.PRE));
    }

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;sendPosition()V", shift = At.Shift.AFTER))
    private void tickHook3(CallbackInfo ci) {
        EVENT_BUS.post(new UpdateWalkingPlayerEvent(Stage.POST));
    }

    @WrapMethod(method = "move")
    private void moveHook(MoverType type, Vec3 movement, Operation<Void> original) {
        MoveEvent event = new MoveEvent(type, movement);
        EVENT_BUS.post(event);
        if (event.isCancelled()) return;
        original.call(type, event.getMovement());
    }
}
