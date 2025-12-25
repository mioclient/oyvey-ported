package me.alpha432.oyvey.util.inventory;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;

public record Result(int slot, ItemStack stack, ResultType type) {
    static Result fromOffhand(ItemStack stack) {
        return new Result(-1, stack, ResultType.OFFHAND);
    }

    public InteractionHand getHand() {
        return type == ResultType.OFFHAND ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND;
    }

    public boolean holding() {
        return type == ResultType.OFFHAND || type == ResultType.HOTBAR && slot == InventoryUtil.selected();
    }

    public boolean found() {
        return type != ResultType.NONE;
    }
}
