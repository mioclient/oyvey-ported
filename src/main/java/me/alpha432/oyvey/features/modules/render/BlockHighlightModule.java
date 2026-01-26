package me.alpha432.oyvey.features.modules.render;

import me.alpha432.oyvey.event.impl.render.Render3DEvent;
import me.alpha432.oyvey.event.impl.render.RenderBlockOutlineEvent;
import me.alpha432.oyvey.event.system.Subscribe;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.settings.Setting;
import me.alpha432.oyvey.util.render.RenderUtil;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.awt.*;

public class BlockHighlightModule extends Module {
    public Setting<Color> color = color("Color", 255, 0, 0, 255);
    public Setting<Float> lineWidth = num("LineWidth", 1.0f, 0.1f, 5.0f);

    public BlockHighlightModule() {
        super("BlockHighlight", "Draws box at the block that you are looking at", Category.RENDER);
    }

    @Subscribe
    public void onRender3D(Render3DEvent event) {
        if (mc.hitResult instanceof BlockHitResult result) {
            VoxelShape shape = mc.level.getBlockState(result.getBlockPos()).getShape(mc.level, result.getBlockPos());
            if (shape.isEmpty()) return;
            AABB box = shape.bounds();
            box = box.move(result.getBlockPos());
            RenderUtil.drawBox(event.getMatrix(), box, color.getValue(), lineWidth.getValue());
        }
    }

    @Subscribe
    public void onRenderBlockOutline(RenderBlockOutlineEvent event) {
        event.cancel();
    }
}
