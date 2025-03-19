package me.alpha432.oyvey.features.modules.movement;

import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.settings.Setting;
import net.minecraft.entity.attribute.EntityAttributes;

public class Step extends Module {
    private final Setting<Float> height = num("Height", 2f, 1f, 3f);

    public Step() {
        super("Step", "step..", Category.MOVEMENT, true, false, false);
    }

    private float prev;

    @Override
    public void onEnable() {
        if (nullCheck()) {
            prev = 0.6f;
            return;
        }
        prev = mc.player.getStepHeight();
    }

    @Override public void onDisable() {
        if (nullCheck()) return;
        mc.player.getAttributeInstance(EntityAttributes.STEP_HEIGHT).setBaseValue(prev);
    }

    @Override public void onUpdate() {
        if (nullCheck()) return;
        mc.player.getAttributeInstance(EntityAttributes.STEP_HEIGHT).setBaseValue(height.getValue());
    }
}
