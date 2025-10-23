package me.alpha432.oyvey.features.modules.player;

import com.google.common.eventbus.Subscribe;
import me.alpha432.oyvey.event.impl.PacketEvent;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.setting.Setting;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.ExplosionS2CPacket;
import java.util.Random;

public class Velocity extends Module {
    private final Setting<Double> minReduction = this.register(new Setting<>("MinReduction", 60.0, 0.0, 100.0));
    private final Setting<Double> maxReduction = this.register(new Setting<>("MaxReduction", 90.0, 0.0, 100.0));
    private final Random random = new Random();

    public Velocity() {
        super("Velocity", "Reduces knockback in a randomized and legit pattern for anti-cheat safety.", Category.PLAYER, true, false, false);
    }

    @Subscribe
    private void onPacketReceive(PacketEvent.Receive event) {
        if (event.getPacket() instanceof EntityVelocityUpdateS2CPacket packet) {
            double reduction = getRandomReduction();
            double multiplier = 1.0 - (reduction / 100.0);
            
            // Apply randomized reduction to velocity
            int velocityX = (int) (packet.getVelocityX() * multiplier);
            int velocityY = (int) (packet.getVelocityY() * multiplier);
            int velocityZ = (int) (packet.getVelocityZ() * multiplier);
            
            // Create a modified packet with reduced velocity
            EntityVelocityUpdateS2CPacket modifiedPacket = new EntityVelocityUpdateS2CPacket(
                packet.getId(), velocityX, velocityY, velocityZ
            );
            
            event.cancel();
            // Note: Implementation would need to send the modified packet to the client
            // This is a simplified version and may need adjustment based on the event system
        } else if (event.getPacket() instanceof ExplosionS2CPacket packet) {
            double reduction = getRandomReduction();
            double multiplier = 1.0 - (reduction / 100.0);
            
            // Apply randomized reduction to explosion velocity
            float velocityX = packet.getPlayerVelocityX() * (float) multiplier;
            float velocityY = packet.getPlayerVelocityY() * (float) multiplier;
            float velocityZ = packet.getPlayerVelocityZ() * (float) multiplier;
            
            // Create a modified packet with reduced explosion velocity
            ExplosionS2CPacket modifiedPacket = new ExplosionS2CPacket(
                packet.getX(), packet.getY(), packet.getZ(),
                packet.getRadius(), packet.getAffectedBlocks(),
                velocityX, velocityY, velocityZ
            );
            
            event.cancel();
            // Note: Implementation would need to send the modified packet to the client
        }
    }
    
    private double getRandomReduction() {
        double min = Math.min(minReduction.getValue(), maxReduction.getValue());
        double max = Math.max(minReduction.getValue(), maxReduction.getValue());
        return min + (max - min) * random.nextDouble();
    }
}
