package me.alpha432.oyvey.util.render.state;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.render.TextureSetup;
import net.minecraft.client.gui.render.state.GuiElementRenderState;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix3x2f;

public record RectRenderState(RenderPipeline pipeline, TextureSetup textureSetup, Matrix3x2f pose, int x0, int y0, int x1, int y1, int topLeft, int bottomLeft, int bottomRight, int topRight, @Nullable ScreenRectangle scissorArea, @Nullable ScreenRectangle bounds) implements GuiElementRenderState {
    public RectRenderState(RenderPipeline renderPipeline, TextureSetup textureSetup, Matrix3x2f matrix3x2f, int x0, int y0, int x1, int y1, int topLeft, int bottomLeft, int bottomRight, int topRight, @Nullable ScreenRectangle screenRectangle) {
        this(renderPipeline, textureSetup, matrix3x2f, x0, y0, x1, y1, topLeft, bottomLeft, bottomRight, topRight, screenRectangle, getBounds(x0, y0, x1, y1, matrix3x2f, screenRectangle));
    }

    public void buildVertices(VertexConsumer vertexConsumer) {
        vertexConsumer.addVertexWith2DPose(this.pose(), (float)this.x0(), (float)this.y0()).setColor(this.topLeft());
        vertexConsumer.addVertexWith2DPose(this.pose(), (float)this.x0(), (float)this.y1()).setColor(this.bottomLeft());
        vertexConsumer.addVertexWith2DPose(this.pose(), (float)this.x1(), (float)this.y1()).setColor(this.bottomRight());
        vertexConsumer.addVertexWith2DPose(this.pose(), (float)this.x1(), (float)this.y0()).setColor(this.topRight());
    }

    @Nullable
    private static ScreenRectangle getBounds(int i, int j, int k, int l, Matrix3x2f matrix3x2f, @Nullable ScreenRectangle screenRectangle) {
        ScreenRectangle screenRectangle2 = (new ScreenRectangle(i, j, k - i, l - j)).transformMaxBounds(matrix3x2f);
        return screenRectangle != null ? screenRectangle.intersection(screenRectangle2) : screenRectangle2;
    }
}
