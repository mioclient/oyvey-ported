package me.alpha432.oyvey.manager;

import me.alpha432.oyvey.event.impl.TickEvent;
import me.alpha432.oyvey.event.system.Subscribe;
import me.alpha432.oyvey.features.Feature;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class HoleManager extends Feature {
    private final int range = 8;
    private final List<Hole> holes = new ArrayList<>();
    private final BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();

    public HoleManager() {
        EVENT_BUS.register(this);
    }

    @Subscribe
    private void onTick(TickEvent event) {
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

    @Nullable
    public Hole getHole(BlockPos pos) {
        if (mc.level.getBlockState(pos).getBlock() != Blocks.AIR)
            return null;
        HoleType type = HoleType.BEDROCK;
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            Block block = mc.level.getBlockState(pos.relative(direction)).getBlock();
            if (block == Blocks.OBSIDIAN) type = HoleType.UNSAFE;
            else if (block != Blocks.BEDROCK) return null;
        }
        return new Hole(pos, type);
    }

    public List<Hole> getHoles() {
        return holes;
    }

    public boolean isHole(BlockPos pos) {
        return holes.stream().anyMatch(hole -> hole.pos().equals(pos));
    }

    public record Hole(BlockPos pos, HoleType holeType) {
    }

    private enum HoleType {
        BEDROCK,
        UNSAFE;
    }
}
