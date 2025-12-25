package me.alpha432.oyvey.features.modules.combat;

import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.settings.Setting;
import me.alpha432.oyvey.util.inventory.InventoryUtil;
import me.alpha432.oyvey.util.inventory.Result;
import net.minecraft.world.item.Items;

import static me.alpha432.oyvey.util.inventory.InventoryUtil.FULL_SCOPE;
import static me.alpha432.oyvey.util.inventory.InventoryUtil.HOTBAR_SCOPE;

public class ClickPearl extends Module {
    private final Setting<Boolean> inventory = bool("Inventory", false);

    public ClickPearl() {
        super("ClickPearl", "Throws a pearl when enabled.", Category.COMBAT);
    }

    @Override
    public void onEnable() {
        disable();

        if (nullCheck()) return;

        int last = InventoryUtil.selected();
        Result result = InventoryUtil.find(Items.ENDER_PEARL, inventory.getValue() ? FULL_SCOPE : HOTBAR_SCOPE);
        if (!result.found())
            return;

        InventoryUtil.swap(result);
        mc.gameMode.useItem(mc.player, result.hand());
        InventoryUtil.swapBack(last, result);
    }
}
