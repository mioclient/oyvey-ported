package me.alpha432.oyvey.util;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import me.alpha432.oyvey.util.traits.Util;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;

public final class EnchantmentUtil implements Util {
    private EnchantmentUtil() {
        throw new IllegalArgumentException("пошел нахуй");
    }

    public static int getLevel(ResourceKey<Enchantment> key, ItemStack stack) {
        if (stack.isEmpty()) return 0;
        for (Object2IntMap.Entry<Holder<Enchantment>> enchantment : stack.getEnchantments().entrySet()) {
            if (enchantment.getKey().is(key)) return enchantment.getIntValue();
        }
        return 0;
    }

    public static int getLevel(ResourceKey<Enchantment> key, EquipmentSlot slot, LivingEntity entity) {
        return getLevel(key, entity.getItemBySlot(slot));
    }

    public static int getLevel(ResourceKey<Enchantment> key, EquipmentSlot slot) {
        return getLevel(key, slot, mc.player);
    }

    public static boolean has(ResourceKey<Enchantment> key, ItemStack stack) {
        return getLevel(key, stack) > 0;
    }

    public static boolean has(ResourceKey<Enchantment> key, EquipmentSlot slot, LivingEntity entity) {
        return getLevel(key, slot, entity) > 0;
    }

    public static boolean has(ResourceKey<Enchantment> key, EquipmentSlot slot) {
        return getLevel(key, slot) > 0;
    }
}
