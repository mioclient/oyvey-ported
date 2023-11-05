package me.alpha432.oyvey.manager;

import com.google.common.eventbus.Subscribe;
import me.alpha432.oyvey.event.impl.UpdateEvent;
import me.alpha432.oyvey.features.Feature;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class HoleManager extends Feature {
    private final int range = 8;
    private final List<Hole> holes = new ArrayList<>();
    private final BlockPos.Mutable pos = new BlockPos.Mutable();

    public HoleManager() {
        EVENT_BUS.register(this);
    }

    @Subscribe private void onTick(UpdateEvent event) {
        holes.clear();
        for (int x = -range; x < range; x++) {
            for (int y = -range; y < range; y++) {
                for (int z = -range; z < range; z++) {
                    pos.set(mc.player.getX() + x, mc.player.getY() + y, mc.player.getZ() + z);
                    Hole hole = getHole(pos);
                    if (hole == null) continue;
                    holes.add(hole);
                }
            }
        }
    }

    @Nullable public Hole getHole(BlockPos pos) {
        if (mc.world.getBlockState(pos).getBlock() != Blocks.AIR)
            return null;
        HoleType type = HoleType.BEDROCK;
        for (Direction direction : Direction.Type.HORIZONTAL) {
            Block block = mc.world.getBlockState(pos.offset(direction)).getBlock();
            if (block == Blocks.OBSIDIAN) type = HoleType.UNSAFE;
            else if (block != Blocks.BEDROCK) return null;
        }
        return new Hole(pos, type);
    }

    private record Hole(BlockPos pos, HoleType holeType) {
    }

    private enum HoleType {
        BEDROCK,
        UNSAFE;
    }
}
