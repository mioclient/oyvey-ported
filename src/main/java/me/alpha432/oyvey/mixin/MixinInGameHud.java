package me.alpha432.oyvey.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import me.alpha432.oyvey.event.impl.Render2DEvent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static me.alpha432.oyvey.util.traits.Util.EVENT_BUS;

@Mixin( InGameHud.class )
public class MixinInGameHud {

    @Inject(method = "render", at = @At("RETURN"))
    public void render(DrawContext context, float tickDelta, CallbackInfo ci) {
        if (MinecraftClient.getInstance().inGameHud.getDebugHud().shouldShowDebugHud()) return;
        RenderSystem.setShaderColor(1, 1, 1, 1);

        RenderSystem.disableDepthTest();
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        RenderSystem.disableCull();
        GL11.glEnable(GL11.GL_LINE_SMOOTH);


        Render2DEvent event = new Render2DEvent(context, tickDelta);
        EVENT_BUS.post(event);

        RenderSystem.enableDepthTest();
        GL11.glDisable(GL11.GL_LINE_SMOOTH);

    }

}
