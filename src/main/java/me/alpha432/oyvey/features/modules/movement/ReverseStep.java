package me.alpha432.oyvey.features.modules.movement;

import me.alpha432.oyvey.features.modules.Module;

public class ReverseStep extends Module {
    public ReverseStep() {
        super("ReverseStep", "step but reversed..", Category.MOVEMENT);
    }

    @Override
    public void onTick() {
        if (nullCheck()) return;
        if (mc.player.isInLava() || mc.player.isInWater() || !mc.player.onGround()) return;
        mc.player.push(0, -1, 0);
    }
}
