package me.alpha432.oyvey.manager;

import me.alpha432.oyvey.features.Feature;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;

public class PositionManager
        extends Feature {
    private double x;
    private double y;
    private double z;
    private boolean onground;

    public void updatePosition() {
        this.x = mc.player.getX();
        this.y = mc.player.getY();
        this.z = mc.player.getZ();
        this.onground = mc.player.isOnGround();
    }

    public void restorePosition() {
        mc.player.setPosition(x, y, z);
        mc.player.setOnGround(onground);
    }

    public void setPlayerPosition(double x, double y, double z) {
        mc.player.setPosition(x, y, z);
    }

    public void setPlayerPosition(double x, double y, double z, boolean onground) {
        mc.player.setPosition(x, y, z);
        mc.player.setOnGround(onground);
    }

    public void setPositionPacket(double x, double y, double z, boolean onGround, boolean setPos, boolean noLagBack) {
        mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(x, y, z, onGround));
        if (setPos) {
            mc.player.setPosition(x, y, z);
            if (noLagBack) {
                this.updatePosition();
            }
        }
    }

    public double getX() {
        return this.x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return this.y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return this.z;
    }

    public void setZ(double z) {
        this.z = z;
    }
}