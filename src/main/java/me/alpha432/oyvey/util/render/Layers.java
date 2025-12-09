package me.alpha432.oyvey.util.render;

import net.minecraft.Util;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;

import java.util.OptionalDouble;
import java.util.function.Function;

import static me.alpha432.oyvey.util.render.Pipelines.GLOBAL_LINES_PIPELINE;
import static me.alpha432.oyvey.util.render.Pipelines.GLOBAL_QUADS_PIPELINE;

public class Layers {
    private static final RenderType GLOBAL_QUADS;
    private static final Function<Double, RenderType> GLOBAL_LINES;

    public static RenderType getGlobalLines(double width) {
        return GLOBAL_LINES.apply(width);
    }

    public static RenderType getGlobalQuads() {
        return GLOBAL_QUADS;
    }

    private static RenderType.CompositeState.CompositeStateBuilder builder() {
        return RenderType.CompositeState.builder();
    }

    private static RenderType.CompositeState empty() {
        return builder().createCompositeState(false);
    }

    static {
        GLOBAL_QUADS = RenderType.create("global_fill", 156, GLOBAL_QUADS_PIPELINE, empty());

        GLOBAL_LINES = Util.memoize(l -> {
            RenderStateShard.LineStateShard width = new RenderStateShard.LineStateShard(OptionalDouble.of(l));
            return RenderType.create("global_lines", 156, GLOBAL_LINES_PIPELINE, builder().setLineState(width).createCompositeState(false));
        });
    }
}
