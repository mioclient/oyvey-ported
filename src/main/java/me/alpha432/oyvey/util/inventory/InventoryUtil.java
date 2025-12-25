package me.alpha432.oyvey.util.inventory;

import me.alpha432.oyvey.util.traits.Util;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.item.ItemStack;

import java.util.Set;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public final class InventoryUtil implements Util {
    public static final Result NONE = new Result(-1, ItemStack.EMPTY, ResultType.NONE);

    private InventoryUtil() {
        throw new AssertionError();
    }

    public static ItemStack cursor() {
        return mc.player.containerMenu.getCarried();
    }

    public static int selected() {
        return mc.player.getInventory().getSelectedSlot();
    }

    public static void click(int slot, int button, ClickType type) {
        int id = mc.player.containerMenu.containerId;
        mc.gameMode.handleInventoryMouseClick(id, slot, button, type, mc.player);
    }

    public static void swap(int to) {
        if (to < 0 || to > 8) return;
        mc.player.getInventory().setSelectedSlot(to);
        mc.gameMode.ensureHasSentCarriedItem();
    }

    public static Result find(Predicate<ItemStack> predicate, ResultType... types) {
        final Set<ResultType> set = Set.of(types);
        return find((item, type) -> set.contains(type) && predicate.test(item));
    }

    public static Result find(BiPredicate<ItemStack, ResultType> predicate) {
        ItemStack offhand = mc.player.getOffhandItem();
        if (predicate.test(offhand, ResultType.OFFHAND)) {
            return Result.fromOffhand(offhand);
        }

        for (int i = 0; i < 9; i++) {
            ItemStack item = mc.player.getInventory().getItem(i);
            if (predicate.test(item, ResultType.HOTBAR)) {
                return new Result(i, item, ResultType.HOTBAR);
            }
        }

        for (int i = 9; i < 36; i++) {
            ItemStack item = mc.player.getInventory().getItem(i);
            if (predicate.test(item, ResultType.INVENTORY)) {
                return new Result(i, item, ResultType.INVENTORY);
            }
        }

        return NONE;
    }
}
