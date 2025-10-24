package me.alpha432.oyvey.features.modules.combat;

import com.google.common.eventbus.Subscribe;
import me.alpha432.oyvey.event.impl.PacketEvent;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.settings.Setting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;

import java.util.Random;

public class Criticals extends Module {
    private final Setting<Double> yOffset = register(new Setting<>("YOffset", 0.05, 0.01, 0.15));
    private final Setting<Integer> chance = register(new Setting<>("Chance", 75, 50, 100));
    private final Random random = new Random();
    private int tickDelay = 0;

    public Criticals() {
        super("Criticals", "Velocity AC bypass - randomized legit criticals", Category.COMBAT, true, false, false);
    }

    @Subscribe
    private void onPacketSend(PacketEvent.Send event) {
        if (event.getPacket() instanceof PlayerInteractEntityC2SPacket packet && packet.type.getType() == PlayerInteractEntityC2SPacket.InteractType.ATTACK) {
            Entity entity = mc.world.getEntityById(packet.entityId);
            
            if (entity == null
                || entity instanceof EndCrystalEntity
                || !mc.player.isOnGround()
                || !(entity instanceof LivingEntity)) return;

            // Velocity AC bypass: only activate on chance % of hits
            if (random.nextInt(100) >= chance.getValue()) return;
            
            // Add tick delay for legitimacy
            if (tickDelay > 0) {
                tickDelay--;
                return;
            }
            tickDelay = random.nextInt(2); // 0-1 tick delay

            // Use randomized Y offset instead of fixed 0.1
            double offset = yOffset.getValue() + (random.nextDouble() * 0.05);
            boolean bl = mc.player.horizontalCollision;
            
            mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(
                mc.player.getX(), 
                mc.player.getY() + offset, 
                mc.player.getZ(), 
                false, 
                bl
            ));
            mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(
                mc.player.getX(), 
                mc.player.getY(), 
                mc.player.getZ(), 
                false, 
                bl
            ));
            mc.player.addCritParticles(entity);
        }
    }

    @Override
    public String getDisplayInfo() {
        return "Bypass";
    }
}
