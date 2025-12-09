package me.alpha432.oyvey.util.render;

import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.platform.DepthTestFunction;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;

import static net.minecraft.client.renderer.RenderPipelines.DEBUG_FILLED_SNIPPET;
import static net.minecraft.client.renderer.RenderPipelines.LINES_SNIPPET;

class Pipelines {
    static final RenderPipeline GLOBAL_QUADS_PIPELINE = RenderPipeline.builder(DEBUG_FILLED_SNIPPET)
            .withLocation("pipeline/global_fill_pipeline")
            .withVertexFormat(DefaultVertexFormat.POSITION_COLOR, VertexFormat.Mode.QUADS)
            .withDepthTestFunction(DepthTestFunction.NO_DEPTH_TEST)
            .withBlend(BlendFunction.TRANSLUCENT)
            .withDepthWrite(false)
            .withCull(false)
            .build();

    static final RenderPipeline GLOBAL_LINES_PIPELINE = RenderPipeline.builder(LINES_SNIPPET)
            .withLocation("pipeline/global_lines_pipeline")
            .withDepthTestFunction(DepthTestFunction.NO_DEPTH_TEST)
            .withBlend(BlendFunction.TRANSLUCENT)
            .withDepthWrite(false)
            .withCull(false)
            .build();
}
