package me.alpha432.oyvey.features.modules.combat;

import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.settings.Setting;
import me.alpha432.oyvey.util.inventory.InventoryUtil;
import me.alpha432.oyvey.util.inventory.Result;
import net.minecraft.world.item.Items;

import static me.alpha432.oyvey.util.inventory.InventoryUtil.FULL_SCOPE;
import static me.alpha432.oyvey.util.inventory.InventoryUtil.HOTBAR_SCOPE;

public class KeyPearl extends Module {
    private final Setting<Boolean> inventory = bool("Inventory", false);

    public KeyPearl() {
        super("KeyPearl", "Throws a pearl when enabled.", Category.COMBAT);
    }

    @Override
    public void onEnable() {
        disable();

        if (nullCheck()) return;

        Result result = InventoryUtil.find(Items.ENDER_PEARL, inventory.getValue() ? FULL_SCOPE : HOTBAR_SCOPE);
        InventoryUtil.withSwap(result, () -> {
            mc.gameMode.useItem(mc.player, result.hand());
        });
    }
}
