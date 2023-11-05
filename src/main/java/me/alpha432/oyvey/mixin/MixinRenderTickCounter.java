package me.alpha432.oyvey.mixin;

import me.alpha432.oyvey.OyVey;
import net.minecraft.client.render.RenderTickCounter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin( RenderTickCounter.class )
public class MixinRenderTickCounter {
    @Shadow public float lastFrameDuration;

    @Inject(method = "beginRenderTick", at = @At(value = "FIELD", target = "Lnet/minecraft/client/render/RenderTickCounter;prevTimeMillis:J"))
    public void beginRenderTick(long timeMillis, CallbackInfoReturnable<Integer> cir) {
        this.lastFrameDuration *= OyVey.TIMER;
    }
}