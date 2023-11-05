package me.alpha432.oyvey.features.modules.player;

import me.alpha432.oyvey.features.modules.Module;
import net.minecraft.item.Items;

public class FastPlace extends Module {
    public FastPlace() {
        super("FastPlace", "", Category.PLAYER, true, false, false);
    }

    @Override public void onUpdate() {
        if (nullCheck()) return;

        if (mc.player.isHolding(Items.EXPERIENCE_BOTTLE)) {
            mc.itemUseCooldown = 0;
        }
    }
}
