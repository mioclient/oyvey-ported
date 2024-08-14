package me.alpha432.oyvey.features.modules.render;

import com.google.common.eventbus.Subscribe;
import me.alpha432.oyvey.event.impl.Render3DEvent;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.util.RenderUtil;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.shape.VoxelShape;

import java.awt.*;

public class BlockHighlight extends Module {
    public BlockHighlight() {
        super("BlockHighlight", "", Category.RENDER, true, false, false);
    }

    @Subscribe public void onRender3D(Render3DEvent event) {
        if (mc.crosshairTarget instanceof BlockHitResult result) {
            VoxelShape shape = mc.world.getBlockState(result.getBlockPos()).getOutlineShape(mc.world, result.getBlockPos());
            if (shape.isEmpty()) return;
            RenderUtil.drawBox(event.getMatrix(), shape.getBoundingBox(), Color.red, 1f);
        }
    }
}
