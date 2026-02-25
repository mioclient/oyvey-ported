package me.alpha432.oyvey.mixin.render;

import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.util.models.Angles;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.world.entity.Avatar;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static me.alpha432.oyvey.util.traits.Util.mc;

@Mixin(AvatarRenderer.class)
public class MixinAvatarRenderer {
    @Inject(method = "extractRenderState(Lnet/minecraft/world/entity/Avatar;Lnet/minecraft/client/renderer/entity/state/AvatarRenderState;F)V", at = @At("TAIL"))
    private <T extends Avatar> void extractRenderStateHook(T avatar, AvatarRenderState state, float f, CallbackInfo ci) {
        if (mc.player != avatar) return;
        Angles lerped = OyVey.rotationManager.getLerpRenderSnapshot(f);
        if (lerped == null) return;
        state.xRot = lerped.xRot();
        state.yRot = 0;
        state.bodyRot = lerped.yRot();
    }
}
