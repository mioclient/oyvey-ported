package me.alpha432.oyvey.manager;
import me.alpha432.oyvey.features.Feature;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;

import java.util.HashMap;

public class SpeedManager
        extends Feature {
    public static final double LAST_JUMP_INFO_DURATION_DEFAULT = 3.0;
    public static boolean didJumpThisTick = false;
    public static boolean isJumping = false;
    private final int distancer = 20;
    public double firstJumpSpeed = 0.0;
    public double lastJumpSpeed = 0.0;
    public double percentJumpSpeedChanged = 0.0;
    public double jumpSpeedChanged = 0.0;
    public boolean didJumpLastTick = false;
    public long jumpInfoStartTime = 0L;
    public boolean wasFirstJump = true;
    public double speedometerCurrentSpeed = 0.0;
    public HashMap<PlayerEntity, Double> playerSpeeds = new HashMap();

    public static void setDidJumpThisTick(boolean val) {
        didJumpThisTick = val;
    }

    public static void setIsJumping(boolean val) {
        isJumping = val;
    }

    public float lastJumpInfoTimeRemaining() {
        return (float) (System.currentTimeMillis() - this.jumpInfoStartTime) / 1000.0f;
    }

    public void updateValues() {
        double distTraveledLastTickX = mc.player.getX() - mc.player.lastX;
        double distTraveledLastTickZ = mc.player.getZ() - mc.player.lastZ;
        this.speedometerCurrentSpeed = distTraveledLastTickX * distTraveledLastTickX + distTraveledLastTickZ * distTraveledLastTickZ;
        if (didJumpThisTick && (!mc.player.isOnGround() || isJumping)) {
            if (didJumpThisTick && !this.didJumpLastTick) {
                this.wasFirstJump = this.lastJumpSpeed == 0.0;
                this.percentJumpSpeedChanged = this.speedometerCurrentSpeed != 0.0 ? this.speedometerCurrentSpeed / this.lastJumpSpeed - 1.0 : -1.0;
                this.jumpSpeedChanged = this.speedometerCurrentSpeed - this.lastJumpSpeed;
                this.jumpInfoStartTime = System.currentTimeMillis();
                this.lastJumpSpeed = this.speedometerCurrentSpeed;
                this.firstJumpSpeed = this.wasFirstJump ? this.lastJumpSpeed : 0.0;
            }
            this.didJumpLastTick = didJumpThisTick;
        } else {
            this.didJumpLastTick = false;
            this.lastJumpSpeed = 0.0;
        }
        this.updatePlayers();
    }

    public void updatePlayers() {
        for (PlayerEntity player : mc.world.getPlayers()) {
            if (!(mc.player.distanceTo(player) < distancer))
                continue;
            double distTraveledLastTickX = player.getX() - player.lastX;
            double distTraveledLastTickZ = player.getZ() - player.lastZ;
            double playerSpeed = distTraveledLastTickX * distTraveledLastTickX + distTraveledLastTickZ * distTraveledLastTickZ;
            this.playerSpeeds.put(player, playerSpeed);
        }
    }

    public double getPlayerSpeed(PlayerEntity player) {
        if (this.playerSpeeds.get(player) == null) {
            return 0.0;
        }
        return this.turnIntoKpH(this.playerSpeeds.get(player));
    }

    public double turnIntoKpH(double input) {
        return Math.sqrt(input) * 71.2729367892;
    }

    public double getSpeedKpH() {
        double speedometerkphdouble = this.turnIntoKpH(this.speedometerCurrentSpeed);
        speedometerkphdouble = (double) Math.round(10.0 * speedometerkphdouble) / 10.0;
        return speedometerkphdouble;
    }

    public double getSpeedMpS() {
        double speedometerMpsdouble = this.turnIntoKpH(this.speedometerCurrentSpeed) / 3.6;
        speedometerMpsdouble = (double) Math.round(10.0 * speedometerMpsdouble) / 10.0;
        return speedometerMpsdouble;
    }
}