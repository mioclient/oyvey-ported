package me.alpha432.oyvey.util.render;

import net.minecraft.client.renderer.rendertype.RenderSetup;
import net.minecraft.client.renderer.rendertype.RenderType;

import static me.alpha432.oyvey.util.render.Pipelines.GLOBAL_LINES_PIPELINE;
import static me.alpha432.oyvey.util.render.Pipelines.GLOBAL_QUADS_PIPELINE;

public class Layers {
    private static final RenderType GLOBAL_QUADS = RenderType.create("global_fill",
            RenderSetup.builder(GLOBAL_QUADS_PIPELINE).createRenderSetup());
    private static final RenderType GLOBAL_LINES = RenderType.create("global_lines",
            RenderSetup.builder(GLOBAL_LINES_PIPELINE).createRenderSetup());

    public static RenderType quads() {
        return GLOBAL_QUADS;
    }

    public static RenderType lines() {
        return GLOBAL_LINES;
    }
}
