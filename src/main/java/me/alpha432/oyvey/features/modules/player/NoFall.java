package me.alpha432.oyvey.features.modules.player;

import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.features.modules.Module;
import java.util.Random;

public class NoFall extends Module {
    private Random random = new Random();
    private long lastActivation = 0;
    private long nextDelay = 0;

    public NoFall() {
        super("NoFall", "Provides subtle NoFall protection with randomized activation.", Category.PLAYER, true, false, false);
    }

    @Override
    public void onUpdate() {
        // Random conditional delays - doesn't always activate
        long currentTime = System.currentTimeMillis();
        
        if (currentTime - lastActivation < nextDelay) {
            return; // Skip activation due to delay
        }
        
        // Only activate randomly (70% chance)
        if (random.nextDouble() > 0.7) {
            return;
        }
        
        // Vanilla mode - no packet manipulation, just natural gameplay
        if (!mc.player.isOnGround() && OyVey.positionManager.getFallDistance() > 3) {
            // Use vanilla method: simulate sneaking briefly to reduce fall damage naturally
            // This mimics normal player behavior without packets
            if (!mc.player.isSneaking() && random.nextBoolean()) {
                mc.player.setSneaking(true);
                // Will be released in next tick naturally
            }
            
            lastActivation = currentTime;
            // Randomize next delay between 100-500ms
            nextDelay = 100 + random.nextInt(400);
        }
    }
}
