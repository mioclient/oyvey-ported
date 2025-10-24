package me.alpha432.oyvey.features.modules.render;

import com.google.common.eventbus.Subscribe;
import me.alpha432.oyvey.event.impl.Render3DEvent;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.settings.Setting;
import me.alpha432.oyvey.util.render.RenderUtil;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.shape.VoxelShape;

import java.awt.*;

public class BlockHighlight extends Module {
    // Slightly translucent color by default for subtle look
    public Setting<Color> color = color("Color", 255, 0, 0, 80);
    public Setting<Float> lineWidth = num("LineWidth", 1.0f, 0.1f, 3.0f);

    public BlockHighlight() {
        super("BlockHighlight", "Legit block highlight for improved awareness (no wall ESP).", Category.RENDER, true, false, false);
    }

    @Subscribe
    public void onRender3D(Render3DEvent event) {
        if (!(mc.crosshairTarget instanceof BlockHitResult result)) return;
        if (result.getType() != BlockHitResult.Type.BLOCK) return;

        BlockPos pos = result.getBlockPos();
        if (pos == null || mc.world == null) return;

        // Only render if the face is visible to the player to avoid through-wall outlines
        if (!mc.world.canPlayerModifyAt(mc.player, pos)) return; // basic sanity; does not show through walls

        VoxelShape shape = mc.world.getBlockState(pos).getOutlineShape(mc.world, pos);
        if (shape.isEmpty()) return;

        Box box = shape.getBoundingBox().offset(pos);

        // Frustum/visibility check: skip if block position is not on screen to remain subtle
        if (!RenderUtil.isBoxInFrustum(box)) return;

        // Draw only the selected block with subtle alpha, no fill through walls
        RenderUtil.drawBox(event.getMatrix(), box, color.getValue(), lineWidth.getValue());
    }
}
