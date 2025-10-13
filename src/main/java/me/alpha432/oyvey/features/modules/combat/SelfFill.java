package me.alpha432.oyvey.features.modules.combat;

import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.settings.Setting;
import me.alpha432.oyvey.util.BlockUtil;
import net.minecraft.block.Blocks;
import net.minecraft.network.packet.PacketType;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;

public class SelfFill extends Module {

    private final Setting<Boolean> packet = register(new Setting<>("PacketPlace", false));

    public SelfFill() {
        super("SelfFill", "SelfFills yourself in a hole.", Category.COMBAT, true, false, false);
    }

    @Override
    public void onEnable() {
        if (mc.player == null) return;
        mc.player.jump();
        mc.player.jump();
    }

    @Override
    public void onUpdate() {
        if (mc.player == null || mc.world == null) return;

        BlockPos playerPos = BlockPos.ofFloored(mc.player.getX(), mc.player.getY(), mc.player.getZ());

        if (mc.world.getBlockState(playerPos.down()).isOf(Blocks.AIR)
                && BlockUtil.isPositionPlaceable(playerPos.down(), false) == 3) {

            BlockUtil.placeBlock(playerPos.down(), Hand.MAIN_HAND, false, packet.getValue(), false);
        }

        if (mc.world.getBlockState(playerPos.down()).isOf(Blocks.OBSIDIAN)) {
            double targetY = mc.player.getY() - 1.3;

            mc.player.networkHandler.sendPacket(
                    new PlayerMoveC2SPacket(
                            mc.player.getX(),
                            targetY,
                            mc.player.getZ(),
                            mc.player.getYaw(),
                            mc.player.getPitch(),
                            false,
                            true,
                            false,
                            false
                    ) {
                        @Override
                        public PacketType<? extends PlayerMoveC2SPacket> getPacketType() {
                            return null;
                        }
                    }
            );

            mc.player.setPosition(mc.player.getX(), targetY, mc.player.getZ());
            toggle();
        }
    }
}