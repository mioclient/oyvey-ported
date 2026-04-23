package me.alpha432.oyvey.features.modules.render;

import me.alpha432.oyvey.event.impl.render.Render3DEvent;
import me.alpha432.oyvey.event.impl.render.RenderBlockOutlineEvent;
import me.alpha432.oyvey.event.system.Subscribe;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.settings.Setting;
import me.alpha432.oyvey.util.render.RenderUtil;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.awt.*;

public class BlockHighlightModule extends Module {
    public final Setting<Color> color = color("Color", 255, 0, 0, 255);
    public final Setting<Float> lineWidth = num("LineWidth", 1.0f, 0.1f, 5.0f);

    public BlockHighlightModule() {
        super("BlockHighlight", "Draws box at the block that you are looking at", Category.RENDER);
    }

    @Subscribe
    public void onRender3D(Render3DEvent event) {
        if (mc.hitResult instanceof BlockHitResult result) {
            VoxelShape shape = mc.level.getBlockState(result.getBlockPos()).getShape(mc.level, result.getBlockPos());
            if (shape.isEmpty() || result.getType() == HitResult.Type.MISS) return;
            AABB aabb = shape.bounds();
            aabb = aabb.move(result.getBlockPos());
            RenderUtil.drawBox(event.getMatrix(), aabb, color.getValue(), lineWidth.getValue());
        }
    }

    @Subscribe
    public void onRenderBlockOutline(RenderBlockOutlineEvent event) {
        event.cancel();
    }
}
