package me.alpha432.oyvey.util.render;

import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.platform.DepthTestFunction;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.resources.Identifier;

import static net.minecraft.client.renderer.RenderPipelines.*;

class Pipelines {
    static final RenderPipeline.Snippet GLOBAL_LINES_SNIPPET = RenderPipeline.builder(MATRICES_FOG_SNIPPET, GLOBALS_SNIPPET)
            .withVertexShader(Identifier.fromNamespaceAndPath("oyvey", "core/rendertype_lines_smooth"))
            .withFragmentShader(Identifier.fromNamespaceAndPath("oyvey", "core/rendertype_lines_smooth"))
            .withVertexFormat(DefaultVertexFormat.POSITION_COLOR_NORMAL_LINE_WIDTH, VertexFormat.Mode.LINES)
            .withBlend(BlendFunction.TRANSLUCENT)
            .withCull(false)
            .buildSnippet();


    static final RenderPipeline GLOBAL_QUADS_PIPELINE = RenderPipeline.builder(DEBUG_FILLED_SNIPPET)
            .withLocation("pipeline/global_fill_pipeline")
            .withVertexFormat(DefaultVertexFormat.POSITION_COLOR, VertexFormat.Mode.QUADS)
            .withDepthTestFunction(DepthTestFunction.NO_DEPTH_TEST)
            .withBlend(BlendFunction.TRANSLUCENT)
            .withDepthWrite(false)
            .withCull(false)
            .build();

    static final RenderPipeline GLOBAL_LINES_PIPELINE = RenderPipeline.builder(GLOBAL_LINES_SNIPPET)
            .withLocation("pipeline/global_lines_pipeline")
            .withDepthTestFunction(DepthTestFunction.NO_DEPTH_TEST)
            .withDepthWrite(false)
            .build();
}
