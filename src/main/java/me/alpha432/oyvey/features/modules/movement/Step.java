package me.alpha432.oyvey.features.modules.movement;

import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.settings.Setting;
import net.minecraft.entity.attribute.EntityAttributes;

import java.util.Random;

public class Step extends Module {
    private final Setting<Float> height = num("Height", 1.25f, 1f, 2f);
    private final Setting<Integer> delay = num("Delay", 5, 2, 15);
    private final Random random = new Random();
    private float prev;
    private int tickCounter = 0;

    public Step() {
        super("Step", "Velocity AC bypass - legit step with delays", Category.MOVEMENT, true, false, false);
    }

    @Override
    public void onEnable() {
        if (nullCheck()) {
            prev = 0.6f;
            return;
        }
        prev = mc.player.getStepHeight();
        tickCounter = 0;
    }

    @Override
    public void onDisable() {
        if (nullCheck()) return;
        mc.player.getAttributeInstance(EntityAttributes.STEP_HEIGHT).setBaseValue(prev);
    }

    @Override
    public void onUpdate() {
        if (nullCheck()) return;
        
        // Velocity AC bypass: Add random delay between step activations
        tickCounter++;
        int requiredDelay = delay.getValue() + random.nextInt(5); // Add 0-4 random ticks
        
        if (tickCounter >= requiredDelay) {
            // Use lower, more legit step height with slight randomization
            float stepHeight = height.getValue() + (random.nextFloat() * 0.25f);
            mc.player.getAttributeInstance(EntityAttributes.STEP_HEIGHT).setBaseValue(Math.min(stepHeight, 2.0f));
            tickCounter = 0;
        } else {
            // Reset to normal during cooldown
            mc.player.getAttributeInstance(EntityAttributes.STEP_HEIGHT).setBaseValue(prev);
        }
    }
}
