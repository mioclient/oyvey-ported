package me.alpha432.oyvey.manager;

import me.alpha432.oyvey.event.Stage;
import me.alpha432.oyvey.event.impl.UpdateWalkingPlayerEvent;
import me.alpha432.oyvey.event.system.Subscribe;
import me.alpha432.oyvey.features.Feature;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;

public class PositionManager
        extends Feature {
    private double x;
    private double y;
    private double z;
    private boolean onground;
    private double fallDistance;

    public PositionManager() {
        EVENT_BUS.register(this);
    }

    @Subscribe
    public void onUpdateWalkingPlayer(UpdateWalkingPlayerEvent event) {
        if (event.getStage() == Stage.POST) return;

        double diff = mc.player.yo - mc.player.getY();
        if (mc.player.onGround() || diff <= 0) {
            fallDistance = 0;
        } else {
            fallDistance += diff;
        }
    }

    public void updatePosition() {
        this.x = mc.player.getX();
        this.y = mc.player.getY();
        this.z = mc.player.getZ();
        this.onground = mc.player.onGround();
    }

    public void restorePosition() {
        mc.player.setPos(x, y, z);
        mc.player.setOnGround(onground);
    }

    public void setPlayerPosition(double x, double y, double z) {
        mc.player.setPos(x, y, z);
    }

    public void setPlayerPosition(double x, double y, double z, boolean onground) {
        mc.player.setPos(x, y, z);
        mc.player.setOnGround(onground);
    }

    public void setPositionPacket(double x, double y, double z, boolean onGround, boolean setPos, boolean noLagBack) {
        boolean bl = mc.player.horizontalCollision;
        mc.player.connection.send(new ServerboundMovePlayerPacket.Pos(x, y, z, onGround, bl));
        if (setPos) {
            mc.player.setPos(x, y, z);
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

    public double getFallDistance() {
        return fallDistance;
    }
}