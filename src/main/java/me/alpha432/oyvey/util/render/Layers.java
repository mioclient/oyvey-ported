package me.alpha432.oyvey.util.render;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderPhase;
import net.minecraft.util.Util;

import java.util.OptionalDouble;
import java.util.function.Function;

import static me.alpha432.oyvey.util.render.Pipelines.*;

public class Layers {
    private static final RenderLayer GLOBAL_QUADS;
    private static final Function<Double, RenderLayer> GLOBAL_LINES;

    public static RenderLayer getGlobalLines(double width) {
        return GLOBAL_LINES.apply(width);
    }

    public static RenderLayer getGlobalQuads() {
        return GLOBAL_QUADS;
    }

    private static RenderLayer.MultiPhaseParameters.Builder builder() {
        return RenderLayer.MultiPhaseParameters.builder();
    }

    private static RenderLayer.MultiPhaseParameters empty() {
        return builder().build(false);
    }

    static {
        GLOBAL_QUADS = RenderLayer.of("global_fill", 156, GLOBAL_QUADS_PIPELINE, empty());

        GLOBAL_LINES = Util.memoize(l -> {
            RenderPhase.LineWidth width = new RenderPhase.LineWidth(OptionalDouble.of(l));
            return RenderLayer.of("global_lines", 156, GLOBAL_LINES_PIPELINE, builder().lineWidth(width).build(false));
        });
    }
}
