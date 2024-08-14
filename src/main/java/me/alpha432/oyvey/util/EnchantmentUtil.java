package me.alpha432.oyvey.util;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import me.alpha432.oyvey.util.traits.Util;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;

public final class EnchantmentUtil implements Util {
    private EnchantmentUtil() {
        throw new IllegalArgumentException("пошел нахуй");
    }

    public static int getLevel(RegistryKey<Enchantment> key, ItemStack stack) {
        if (stack.isEmpty()) return 0;
        for (Object2IntMap.Entry<RegistryEntry<Enchantment>> enchantment : stack.getEnchantments().getEnchantmentEntries()) {
            if (enchantment.getKey().matchesKey(key)) return enchantment.getIntValue();
        }
        return 0;
    }

    public static int getLevel(RegistryKey<Enchantment> key, EquipmentSlot slot, LivingEntity entity) {
        return getLevel(key, entity.getEquippedStack(slot));
    }

    public static int getLevel(RegistryKey<Enchantment> key, EquipmentSlot slot) {
        return getLevel(key, slot, mc.player);
    }

    public static boolean has(RegistryKey<Enchantment> key, ItemStack stack) {
        return getLevel(key, stack) > 0;
    }

    public static boolean has(RegistryKey<Enchantment> key, EquipmentSlot slot, LivingEntity entity) {
        return getLevel(key, slot, entity) > 0;
    }

    public static boolean has(RegistryKey<Enchantment> key, EquipmentSlot slot) {
        return getLevel(key, slot) > 0;
    }
}
