package me.alpha432.oyvey.mixin;
import com.llamalad7.mixinextras.sugar.Local;
import me.alpha432.oyvey.event.impl.Render3DEvent;
import net.minecraft.client.render.*;
import net.minecraft.client.util.ObjectAllocator;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.profiler.Profiler;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static me.alpha432.oyvey.util.traits.Util.EVENT_BUS;
import static me.alpha432.oyvey.util.traits.Util.mc;

@Mixin( WorldRenderer.class )
public class MixinWorldRenderer {
    @Inject(method = "render", at = @At("RETURN"))
    private void render(ObjectAllocator allocator, RenderTickCounter tickCounter, boolean renderBlockOutline,
                        Camera camera, GameRenderer gameRenderer, Matrix4f positionMatrix, Matrix4f projectionMatrix, CallbackInfo ci,
                        @Local Profiler profiler) {
        MatrixStack stack = new MatrixStack();
        stack.push();
        stack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(mc.gameRenderer.getCamera().getPitch()));
        stack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(mc.gameRenderer.getCamera().getYaw() + 180f));

        profiler.push("oyvey-render-3d");

        Render3DEvent event = new Render3DEvent(stack, tickCounter.getTickProgress(true));
        EVENT_BUS.post(event);
        stack.pop();
        profiler.pop();
    }
}