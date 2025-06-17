package me.alpha432.oyvey.features.modules.misc;

import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.settings.Setting;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.NoteBlockBlockEntity;
import net.minecraft.util.math.BlockPos;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class Notebot extends Module {

    private final Setting<Integer> range = this.register(new Setting<>("Range", 5, 1, 10));

    public Notebot() {
        super("Notebot", "Plays nearby noteblocks", Category.MISC, true, false, false);
    }

    @Override
    public void onTick() {
        if (mc.world == null || mc.player == null) {
            return;
        }

        if (!isEnabled()) {
            return;
        }

        List<NoteBlockBlockEntity> noteblocks = StreamSupport.stream(mc.world.blockEntities.spliterator(), false)
                .filter(blockEntity -> blockEntity instanceof NoteBlockBlockEntity)
                .map(blockEntity -> (NoteBlockBlockEntity) blockEntity)
                .filter(noteBlock -> mc.player.squaredDistanceTo(noteBlock.getPos().getX(), noteBlock.getPos().getY(), noteBlock.getPos().getZ()) <= range.getValue() * range.getValue())
                .sorted(Comparator.comparingDouble(noteBlock -> mc.player.squaredDistanceTo(noteBlock.getPos().getX(), noteBlock.getPos().getY(), noteBlock.getPos().getZ())))
                .collect(Collectors.toList());

        for (NoteBlockBlockEntity noteblock : noteblocks) {
            // TODO: Implement interaction logic (e.g., right-clicking the noteblock)
            // For now, just print a message
            OyVey.LOGGER.info("Found noteblock at: " + noteblock.getPos().toString());
            // Example of how one might interact (actual interaction needs to be implemented):
            // mc.interactionManager.interactBlock(mc.player, mc.world, Hand.MAIN_HAND, new BlockHitResult(Vec3d.of(noteblock.getPos()), Direction.UP, noteblock.getPos(), false));
        }
    }
}
