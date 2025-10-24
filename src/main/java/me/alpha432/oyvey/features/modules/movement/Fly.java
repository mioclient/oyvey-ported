package me.alpha432.oyvey.features.modules.movement;

import com.google.common.eventbus.Subscribe;
import me.alpha432.oyvey.event.impl.MotionEvent;
import me.alpha432.oyvey.event.impl.PacketEvent;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.settings.Setting;
import me.alpha432.oyvey.util.timer.Timer;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.Vec3d;

public class Fly extends Module {
    public Setting<Mode> mode = new Setting<>("Mode", Mode.BYPASS);
    public Setting<Float> speed = new Setting<>("Speed", 0.5f, 0.1f, 2.0f);
    public Setting<Float> maxHeight = new Setting<>("MaxHeight", 5.0f, 1.0f, 10.0f);
    
    private Timer stopTimer = new Timer();
    private Timer packetTimer = new Timer();
    private boolean shouldStop = false;
    private double startY;
    private int ticksFlying = 0;
    
    public enum Mode {
        VANILLA,
        PACKET, 
        BYPASS
    }
    
    public Fly() {
        super("Fly", "Fly module with anti-cheat bypass - mimics legit low-altitude and slow movement.", Category.MOVEMENT, true, false, false);
    }
    
    @Override
    public void onEnable() {
        if (mc.player == null) return;
        startY = mc.player.getY();
        ticksFlying = 0;
        shouldStop = false;
        stopTimer.reset();
        packetTimer.reset();
    }
    
    @Override
    public void onDisable() {
        if (mc.player == null) return;
        mc.player.getAbilities().flying = false;
        mc.player.getAbilities().allowFlying = false;
    }
    
    @Subscribe
    public void onMotion(MotionEvent event) {
        if (mc.player == null || mc.world == null) return;
        
        ticksFlying++;
        
        switch (mode.getValue()) {
            case VANILLA:
                handleVanilla();
                break;
            case PACKET:
                handlePacket();
                break;
            case BYPASS:
                handleBypass();
                break;
        }
    }
    
    private void handleVanilla() {
        mc.player.getAbilities().flying = true;
        mc.player.getAbilities().allowFlying = true;
        mc.player.getAbilities().setFlySpeed(speed.getValue() * 0.1f);
    }
    
    private void handlePacket() {
        if (mc.options.jumpKey.isPressed()) {
            mc.player.setVelocity(mc.player.getVelocity().x, speed.getValue(), mc.player.getVelocity().z);
        } else if (mc.options.sneakKey.isPressed()) {
            mc.player.setVelocity(mc.player.getVelocity().x, -speed.getValue(), mc.player.getVelocity().z);
        } else {
            mc.player.setVelocity(mc.player.getVelocity().x, 0, mc.player.getVelocity().z);
        }
        
        Vec3d motion = mc.player.getVelocity();
        mc.player.setVelocity(motion.x * speed.getValue(), motion.y, motion.z * speed.getValue());
    }
    
    private void handleBypass() {
        // Limit max height to avoid triggering anti-cheat
        if (mc.player.getY() - startY > maxHeight.getValue()) {
            mc.player.setVelocity(mc.player.getVelocity().x, -0.1, mc.player.getVelocity().z);
            return;
        }
        
        // Periodic stop/start to mimic natural movement
        if (stopTimer.passedMs(3000 + (int)(Math.random() * 2000))) {
            shouldStop = !shouldStop;
            stopTimer.reset();
        }
        
        if (shouldStop) {
            // Brief pause to mimic landing/resting
            mc.player.setVelocity(mc.player.getVelocity().x * 0.8, -0.08, mc.player.getVelocity().z * 0.8);
            return;
        }
        
        // Very slow, controlled movement
        double motionY = 0;
        if (mc.options.jumpKey.isPressed()) {
            motionY = Math.min(0.2, speed.getValue() * 0.4); // Slow ascent
        } else if (mc.options.sneakKey.isPressed()) {
            motionY = Math.max(-0.2, -speed.getValue() * 0.4); // Slow descent
        } else {
            // Slight bobbing to mimic natural movement
            motionY = Math.sin(ticksFlying * 0.1) * 0.02;
        }
        
        // Horizontal movement with reduced speed
        Vec3d motion = mc.player.getVelocity();
        double horizontalSpeed = speed.getValue() * 0.3; // Much slower horizontal
        
        mc.player.setVelocity(motion.x * horizontalSpeed, motionY, motion.z * horizontalSpeed);
    }
    
    @Subscribe
    public void onPacketSend(PacketEvent.Send event) {
        if (mode.getValue() == Mode.BYPASS && event.getPacket() instanceof PlayerMoveC2SPacket) {
            // Add small delays between packets to avoid spam detection
            if (!packetTimer.passedMs(50)) {
                event.cancel();
            } else {
                packetTimer.reset();
            }
        }
    }
}
