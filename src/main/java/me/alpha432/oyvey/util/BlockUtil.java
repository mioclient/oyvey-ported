package me.alpha432.oyvey.util;

import com.google.common.collect.Sets;
import com.google.common.eventbus.EventBus;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.HandSwingC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.Set;

public final class BlockUtil {

    private static final MinecraftClient MC = MinecraftClient.getInstance();

    private static final Set<Block> BLACK_LIST = Sets.newHashSet(
            Blocks.ANVIL, Blocks.CHIPPED_ANVIL, Blocks.DAMAGED_ANVIL,
            Blocks.MUSHROOM_STEM, Blocks.BROWN_MUSHROOM_BLOCK, Blocks.RED_MUSHROOM_BLOCK,
            Blocks.MELON, Blocks.PUMPKIN, Blocks.CARVED_PUMPKIN, Blocks.JACK_O_LANTERN,
            Blocks.BEDROCK
    );

    private static final Set<Block> SHULKER_LIST = Sets.newHashSet(
            Blocks.SHULKER_BOX, Blocks.WHITE_SHULKER_BOX, Blocks.ORANGE_SHULKER_BOX,
            Blocks.MAGENTA_SHULKER_BOX, Blocks.LIGHT_BLUE_SHULKER_BOX,
            Blocks.YELLOW_SHULKER_BOX, Blocks.LIME_SHULKER_BOX, Blocks.PINK_SHULKER_BOX,
            Blocks.GRAY_SHULKER_BOX, Blocks.LIGHT_GRAY_SHULKER_BOX, Blocks.CYAN_SHULKER_BOX,
            Blocks.PURPLE_SHULKER_BOX, Blocks.BLUE_SHULKER_BOX, Blocks.BROWN_SHULKER_BOX,
            Blocks.GREEN_SHULKER_BOX, Blocks.RED_SHULKER_BOX, Blocks.BLACK_SHULKER_BOX
    );

    public static boolean placeBlock(BlockPos pos,
                                     Hand hand,
                                     boolean rotate,
                                     boolean packet,
                                     boolean sneaking) {

        if (MC.world == null || MC.player == null || MC.interactionManager == null) return false;

        BlockState state = MC.world.getBlockState(pos);
        ItemStack held = MC.player.getStackInHand(hand);

        if (!state.canReplace(new ItemPlacementContext(MC.player, hand, held,
                new BlockHitResult(Vec3d.ofCenter(pos), Direction.UP, pos, false)))) {
            return false;
        }

        Direction side = getPlaceableSide(pos);
        if (side == null) return false;

        BlockPos neighbour = pos.offset(side);
        Direction opposite = side.getOpposite();
        Vec3d hitVec = Vec3d.ofCenter(neighbour).add(Vec3d.of(opposite.getVector()).multiply(0.5));

        boolean needSneak = shouldSneak(MC.world.getBlockState(neighbour).getBlock());
        boolean wasSneaking = MC.player.isSneaking();
        if (needSneak && !wasSneaking) {
            if (packet) {
                MC.player.networkHandler.sendPacket(
                        new ClientCommandC2SPacket(MC.player, ClientCommandC2SPacket.Mode.PRESS_SHIFT_KEY));
            } else {
                MC.player.setSneaking(true);
            }
        }

        int oldSlot = MC.player.getInventory().getSelectedSlot();
        int blockSlot = findBlockInHotbar();
        if (blockSlot == -1) return false;

        if (oldSlot != blockSlot) {
            if (packet) {
                MC.player.networkHandler.sendPacket(new UpdateSelectedSlotC2SPacket(blockSlot));
            } else {
                MC.player.getInventory().setSelectedSlot(blockSlot);
            }
        }

        BlockHitResult hitResult = new BlockHitResult(hitVec, opposite, neighbour, false);
        ActionResult result;
        if (packet) {
            MC.player.networkHandler.sendPacket(new PlayerInteractBlockC2SPacket(hand, hitResult, 0));
            MC.player.networkHandler.sendPacket(new HandSwingC2SPacket(hand));
            result = ActionResult.SUCCESS;
        } else {
            result = MC.interactionManager.interactBlock(MC.player, hand, hitResult);
            if (result == ActionResult.SUCCESS) MC.player.swingHand(hand);
        }

        if (oldSlot != blockSlot) {
            if (packet) {
                MC.player.networkHandler.sendPacket(new UpdateSelectedSlotC2SPacket(oldSlot));
            } else {
                MC.player.getInventory().setSelectedSlot(oldSlot);
            }
        }

        if (needSneak && !wasSneaking) {
            if (packet) {
                MC.player.networkHandler.sendPacket(
                        new ClientCommandC2SPacket(MC.player, ClientCommandC2SPacket.Mode.RELEASE_SHIFT_KEY));
            } else {
                MC.player.setSneaking(false);
            }
        }

        return result.isAccepted();
    }

    public static int isPositionPlaceable(BlockPos pos, boolean rayTrace) {
        return getPlaceableSide(pos) != null ? 3 : 0;
    }

    private static Direction getPlaceableSide(BlockPos pos) {
        World world = MC.world;
        if (world == null) return null;

        for (Direction dir : Direction.values()) {
            BlockPos neighbour = pos.offset(dir);
            BlockState neighbourState = world.getBlockState(neighbour);
            if (neighbourState.isAir()) continue;
            if (neighbourState.getOutlineShape(world, neighbour).isEmpty()) continue;
            return dir;
        }
        return null;
    }

    private static int findBlockInHotbar() {
        for (int i = 0; i < 9; i++) {
            ItemStack stack = MC.player.getInventory().getStack(i);
            if (stack.getItem() instanceof BlockItem bi) {
                Block bl = bi.getBlock();
                if (BLACK_LIST.contains(bl) || SHULKER_LIST.contains(bl)) continue;
                return i;
            }
        }
        return -1;
    }

    private static boolean shouldSneak(Block block) {
        return BLACK_LIST.contains(block) || SHULKER_LIST.contains(block);
    }

    private BlockUtil() {}

    public static void placeBlock(BlockPos down, Boolean min, boolean b, Boolean value, boolean b1, EventBus eventBus) {
    }
}