package me.alpha432.oyvey.mixin;
import com.mojang.blaze3d.systems.RenderSystem;
import me.alpha432.oyvey.event.impl.Render3DEvent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static me.alpha432.oyvey.util.traits.Util.EVENT_BUS;

@Mixin( WorldRenderer.class )
public class MixinWorldRenderer {
    @Inject(method = "render", at = @At("RETURN"))
    private void render(MatrixStack matrices, float tickDelta, long limitTime, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightmapTextureManager lightmapTextureManager, Matrix4f positionMatrix, CallbackInfo ci) {
        MinecraftClient.getInstance().getProfiler().push("oyvey-render-3d");
        RenderSystem.clear(GL11.GL_DEPTH_BUFFER_BIT, MinecraftClient.IS_SYSTEM_MAC);
        Render3DEvent event = new Render3DEvent(matrices, tickDelta);
        EVENT_BUS.post(event);
        MinecraftClient.getInstance().getProfiler().pop();
    }
}