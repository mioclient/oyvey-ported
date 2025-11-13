package me.alpha432.oyvey.util.render;

import com.mojang.blaze3d.vertex.VertexFormat;
import me.alpha432.oyvey.util.traits.Util;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;

import java.awt.*;
import java.util.Objects;

public class RenderUtil implements Util {

    public static void rect(DrawContext context, float x1, float y1, float x2, float y2, int color) {
        int ix1 = Math.round(x1);
        int iy1 = Math.round(y1);
        int ix2 = Math.round(x2);
        int iy2 = Math.round(y2);
        context.fill(ix1, iy1, ix2, iy2, color);
    }

    public static void rect(DrawContext context, float x1, float y1, float x2, float y2, int color, float width) {
        int w = Math.max(1, Math.round(width));
        context.fill(Math.round(x1), Math.round(y1), Math.round(x2), Math.round(y1) + w, color);
        context.fill(Math.round(x2) - w, Math.round(y1), Math.round(x2), Math.round(y2), color);
        context.fill(Math.round(x1), Math.round(y2) - w, Math.round(x2), Math.round(y2), color);
        context.fill(Math.round(x1), Math.round(y1), Math.round(x1) + w, Math.round(y2), color);
    }

    public static void horizontalGradient(DrawContext context, float x1, float y1, float x2, float y2, Color left, Color right) {
        int ix1 = Math.round(x1);
        int iy1 = Math.round(y1);
        int ix2 = Math.round(x2);
        int iy2 = Math.round(y2);
        int width = Math.max(1, ix2 - ix1);
        for (int i = 0; i < width; i++) {
            float t = width == 1 ? 1f : (float) i / (float) (width - 1);
            int r = (int) (left.getRed() + (right.getRed() - left.getRed()) * t);
            int g = (int) (left.getGreen() + (right.getGreen() - left.getGreen()) * t);
            int b = (int) (left.getBlue() + (right.getBlue() - left.getBlue()) * t);
            int a = (int) (left.getAlpha() + (right.getAlpha() - left.getAlpha()) * t);
            int argb = (a & 0xFF) << 24 | (r & 0xFF) << 16 | (g & 0xFF) << 8 | (b & 0xFF);
            context.fill(ix1 + i, iy1, ix1 + i + 1, iy2, argb);
        }
    }

    public static void verticalGradient(DrawContext context, float x1, float y1, float x2, float y2, Color top, Color bottom) {
        int ix1 = Math.round(x1);
        int iy1 = Math.round(y1);
        int ix2 = Math.round(x2);
        int iy2 = Math.round(y2);
        int height = Math.max(1, iy2 - iy1);
        for (int i = 0; i < height; i++) {
            float t = height == 1 ? 1f : (float) i / (float) (height - 1);
            int r = (int) (top.getRed() + (bottom.getRed() - top.getRed()) * t);
            int g = (int) (top.getGreen() + (bottom.getGreen() - top.getGreen()) * t);
            int b = (int) (top.getBlue() + (bottom.getBlue() - top.getBlue()) * t);
            int a = (int) (top.getAlpha() + (bottom.getAlpha() - top.getAlpha()) * t);
            int argb = (a & 0xFF) << 24 | (r & 0xFF) << 16 | (g & 0xFF) << 8 | (b & 0xFF);
            context.fill(ix1, iy1 + i, ix2, iy1 + i + 1, argb);
        }
    }

    public static void rect(MatrixStack stack, float x1, float y1, float x2, float y2, int color) {
        rectFilled(stack, x1, y1, x2, y2, color);
    }

    public static void rect(MatrixStack stack, float x1, float y1, float x2, float y2, int color, float width) {
        drawHorizontalLine(stack, x1, x2, y1, color, width);
        drawVerticalLine(stack, x2, y1, y2, color, width);
        drawHorizontalLine(stack, x1, x2, y2, color, width);
        drawVerticalLine(stack, x1, y1, y2, color, width);
    }

    protected static void drawHorizontalLine(MatrixStack matrices, float x1, float x2, float y, int color) {
        if (x2 < x1) {
            float i = x1;
            x1 = x2;
            x2 = i;
        }

        rectFilled(matrices, x1, y, x2 + 1, y + 1, color);
    }

    protected static void drawVerticalLine(MatrixStack matrices, float x, float y1, float y2, int color) {
        if (y2 < y1) {
            float i = y1;
            y1 = y2;
            y2 = i;
        }

        rectFilled(matrices, x, y1 + 1, x + 1, y2, color);
    }

    protected static void drawHorizontalLine(MatrixStack matrices, float x1, float x2, float y, int color, float width) {
        if (x2 < x1) {
            float i = x1;
            x1 = x2;
            x2 = i;
        }

        rectFilled(matrices, x1, y, x2 + width, y + width, color);
    }

    protected static void drawVerticalLine(MatrixStack matrices, float x, float y1, float y2, int color, float width) {
        if (y2 < y1) {
            float i = y1;
            y1 = y2;
            y2 = i;
        }

        rectFilled(matrices, x, y1 + width, x + width, y2, color);
    }

    public static void rectFilled(MatrixStack matrix, float x1, float y1, float x2, float y2, int color) {
        float i;
        if (x1 < x2) {
            i = x1;
            x1 = x2;
            x2 = i;
        }

        if (y1 < y2) {
            i = y1;
            y1 = y2;
            y2 = i;
        }

        float f = (float) (color >> 24 & 255) / 255.0F;
        float g = (float) (color >> 16 & 255) / 255.0F;
        float h = (float) (color >> 8 & 255) / 255.0F;
        float j = (float) (color & 255) / 255.0F;

        BufferBuilder bufferBuilder = Tessellator.getInstance()
                .begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
        bufferBuilder.vertex(matrix.peek().getPositionMatrix(), x1, y2, 0.0F).color(g, h, j, f);
        bufferBuilder.vertex(matrix.peek().getPositionMatrix(), x2, y2, 0.0F).color(g, h, j, f);
        bufferBuilder.vertex(matrix.peek().getPositionMatrix(), x2, y1, 0.0F).color(g, h, j, f);
        bufferBuilder.vertex(matrix.peek().getPositionMatrix(), x1, y1, 0.0F).color(g, h, j, f);

        Layers.getGlobalQuads().draw(bufferBuilder.end());
    }

    public static void horizontalGradient(MatrixStack matrix, float x1, float y1, float x2, float y2, Color left, Color right) {
        BufferBuilder bufferBuilder = Tessellator.getInstance()
                .begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
        bufferBuilder.vertex(matrix.peek().getPositionMatrix(), x1, y1, 0.0F).color(left.getRed() / 255.0F, left.getGreen() / 255.0F, left.getBlue() / 255.0F, left.getAlpha() / 255.0F);
        bufferBuilder.vertex(matrix.peek().getPositionMatrix(), x1, y2, 0.0F).color(left.getRed() / 255.0F, left.getGreen() / 255.0F, left.getBlue() / 255.0F, left.getAlpha() / 255.0F);
        bufferBuilder.vertex(matrix.peek().getPositionMatrix(), x2, y2, 0.0F).color(right.getRed() / 255.0F, right.getGreen() / 255.0F, right.getBlue() / 255.0F, right.getAlpha() / 255.0F);
        bufferBuilder.vertex(matrix.peek().getPositionMatrix(), x2, y1, 0.0F).color(right.getRed() / 255.0F, right.getGreen() / 255.0F, right.getBlue() / 255.0F, right.getAlpha() / 255.0F);

        Layers.getGlobalQuads().draw(bufferBuilder.end());
    }

    public static void verticalGradient(MatrixStack matrix, float x1, float y1, float x2, float y2, Color top, Color bottom) {
        BufferBuilder bufferBuilder = Tessellator.getInstance()
                .begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
        bufferBuilder.vertex(matrix.peek().getPositionMatrix(), x1, y1, 0.0F).color(top.getRed() / 255.0F, top.getGreen() / 255.0F, top.getBlue() / 255.0F, top.getAlpha() / 255.0F);
        bufferBuilder.vertex(matrix.peek().getPositionMatrix(), x1, y2, 0.0F).color(bottom.getRed() / 255.0F, bottom.getGreen() / 255.0F, bottom.getBlue() / 255.0F, bottom.getAlpha() / 255.0F);
        bufferBuilder.vertex(matrix.peek().getPositionMatrix(), x2, y2, 0.0F).color(bottom.getRed() / 255.0F, bottom.getGreen() / 255.0F, bottom.getBlue() / 255.0F, bottom.getAlpha() / 255.0F);
        bufferBuilder.vertex(matrix.peek().getPositionMatrix(), x2, y1, 0.0F).color(top.getRed() / 255.0F, top.getGreen() / 255.0F, top.getBlue() / 255.0F, top.getAlpha() / 255.0F);

        Layers.getGlobalQuads().draw(bufferBuilder.end());
    }

    // 3d
    public static void drawBoxFilled(MatrixStack stack, Box box, Color c) {
        float minX = (float) (box.minX - mc.getEntityRenderDispatcher().camera.getPos().getX());
        float minY = (float) (box.minY - mc.getEntityRenderDispatcher().camera.getPos().getY());
        float minZ = (float) (box.minZ - mc.getEntityRenderDispatcher().camera.getPos().getZ());
        float maxX = (float) (box.maxX - mc.getEntityRenderDispatcher().camera.getPos().getX());
        float maxY = (float) (box.maxY - mc.getEntityRenderDispatcher().camera.getPos().getY());
        float maxZ = (float) (box.maxZ - mc.getEntityRenderDispatcher().camera.getPos().getZ());

        BufferBuilder bufferBuilder = Tessellator.getInstance()
                .begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
        bufferBuilder.vertex(stack.peek().getPositionMatrix(), minX, minY, minZ).color(c.getRGB());
        bufferBuilder.vertex(stack.peek().getPositionMatrix(), maxX, minY, minZ).color(c.getRGB());
        bufferBuilder.vertex(stack.peek().getPositionMatrix(), maxX, minY, maxZ).color(c.getRGB());
        bufferBuilder.vertex(stack.peek().getPositionMatrix(), minX, minY, maxZ).color(c.getRGB());

        bufferBuilder.vertex(stack.peek().getPositionMatrix(), minX, maxY, minZ).color(c.getRGB());
        bufferBuilder.vertex(stack.peek().getPositionMatrix(), minX, maxY, maxZ).color(c.getRGB());
        bufferBuilder.vertex(stack.peek().getPositionMatrix(), maxX, maxY, maxZ).color(c.getRGB());
        bufferBuilder.vertex(stack.peek().getPositionMatrix(), maxX, maxY, minZ).color(c.getRGB());

        bufferBuilder.vertex(stack.peek().getPositionMatrix(), minX, minY, minZ).color(c.getRGB());
        bufferBuilder.vertex(stack.peek().getPositionMatrix(), minX, maxY, minZ).color(c.getRGB());
        bufferBuilder.vertex(stack.peek().getPositionMatrix(), maxX, maxY, minZ).color(c.getRGB());
        bufferBuilder.vertex(stack.peek().getPositionMatrix(), maxX, minY, minZ).color(c.getRGB());

        bufferBuilder.vertex(stack.peek().getPositionMatrix(), maxX, minY, minZ).color(c.getRGB());
        bufferBuilder.vertex(stack.peek().getPositionMatrix(), maxX, maxY, minZ).color(c.getRGB());
        bufferBuilder.vertex(stack.peek().getPositionMatrix(), maxX, maxY, maxZ).color(c.getRGB());
        bufferBuilder.vertex(stack.peek().getPositionMatrix(), maxX, minY, maxZ).color(c.getRGB());

        bufferBuilder.vertex(stack.peek().getPositionMatrix(), minX, minY, maxZ).color(c.getRGB());
        bufferBuilder.vertex(stack.peek().getPositionMatrix(), maxX, minY, maxZ).color(c.getRGB());
        bufferBuilder.vertex(stack.peek().getPositionMatrix(), maxX, maxY, maxZ).color(c.getRGB());
        bufferBuilder.vertex(stack.peek().getPositionMatrix(), minX, maxY, maxZ).color(c.getRGB());

        bufferBuilder.vertex(stack.peek().getPositionMatrix(), minX, minY, minZ).color(c.getRGB());
        bufferBuilder.vertex(stack.peek().getPositionMatrix(), minX, minY, maxZ).color(c.getRGB());
        bufferBuilder.vertex(stack.peek().getPositionMatrix(), minX, maxY, maxZ).color(c.getRGB());
        bufferBuilder.vertex(stack.peek().getPositionMatrix(), minX, maxY, minZ).color(c.getRGB());

        Layers.getGlobalQuads().draw(bufferBuilder.end());
    }

    public static void drawBoxFilled(MatrixStack stack, Vec3d vec, Color c) {
        drawBoxFilled(stack, Box.from(vec), c);
    }

    public static void drawBoxFilled(MatrixStack stack, BlockPos bp, Color c) {
        drawBoxFilled(stack, new Box(bp), c);
    }

    public static void drawBox(MatrixStack stack, Box box, Color c, double lineWidth) {
        float minX = (float) (box.minX - mc.getEntityRenderDispatcher().camera.getPos().getX());
        float minY = (float) (box.minY - mc.getEntityRenderDispatcher().camera.getPos().getY());
        float minZ = (float) (box.minZ - mc.getEntityRenderDispatcher().camera.getPos().getZ());
        float maxX = (float) (box.maxX - mc.getEntityRenderDispatcher().camera.getPos().getX());
        float maxY = (float) (box.maxY - mc.getEntityRenderDispatcher().camera.getPos().getY());
        float maxZ = (float) (box.maxZ - mc.getEntityRenderDispatcher().camera.getPos().getZ());

        BufferBuilder bufferBuilder = Tessellator.getInstance()
                .begin(VertexFormat.DrawMode.LINES, VertexFormats.POSITION_COLOR_NORMAL);

        VertexRendering.drawBox(stack.peek(), bufferBuilder, minX, minY, minZ, maxX, maxY, maxZ,
                c.getRed() / 255f, c.getGreen() / 255f, c.getBlue() / 255f, c.getAlpha() / 255f);

        Layers.getGlobalLines(lineWidth).draw(bufferBuilder.end());
    }

    public static void drawBox(MatrixStack stack, Vec3d vec, Color c, double lineWidth) {
        drawBox(stack, Box.from(vec), c, lineWidth);
    }

    public static void drawBox(MatrixStack stack, BlockPos bp, Color c, double lineWidth) {
        drawBox(stack, new Box(bp), c, lineWidth);
    }

    public static MatrixStack matrixFrom(Vec3d pos) {
        MatrixStack matrices = new MatrixStack();
        Camera camera = mc.gameRenderer.getCamera();
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(camera.getPitch()));
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(camera.getYaw() + 180.0F));
        matrices.translate(pos.getX() - camera.getPos().x, pos.getY() - camera.getPos().y, pos.getZ() - camera.getPos().z);
        return matrices;
    }
}
