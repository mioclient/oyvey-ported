package me.alpha432.oyvey.manager;

import me.alpha432.oyvey.event.Stage;
import me.alpha432.oyvey.event.impl.entity.player.TickEvent;
import me.alpha432.oyvey.event.impl.entity.player.TravelEvent;
import me.alpha432.oyvey.event.impl.entity.player.UpdateWalkingPlayerEvent;
import me.alpha432.oyvey.event.system.Subscribe;
import me.alpha432.oyvey.util.models.Angles;
import me.alpha432.oyvey.util.traits.Util;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;
import net.minecraft.util.Mth;

import java.util.*;
import java.util.function.Predicate;

public class RotationManager implements Util {
    private final List<Callback> callbacks = new ArrayList<>();

    private boolean updateRender;
    private Angles renderSnapshot, renderSnapshot0;

    private Angles snapshot;
    private Angles travelSnapshot;

    public RotationManager() {
        EVENT_BUS.register(this);
    }

    @Subscribe(priority = 1000)
    public void onTickPre(TickEvent.Pre event) {
        executeCallbacks(callback -> callback.type() == Type.SYNC);
    }

    @Subscribe
    public void onTravelPre(TravelEvent.Pre event) {
        if (callbacks.isEmpty())
            return;

        Callback highest = callbacks.stream().max(Comparator.comparing(Callback::priority)).orElseThrow();
        if (highest.type() == Type.SYNC) {
            travelSnapshot = new Angles(mc.player.getYRot(), mc.player.getXRot());
            mc.player.setYRot(highest.angles().yRot());
            mc.player.setXRot(highest.angles().xRot());
        }
    }

    @Subscribe
    public void onTravelPost(TravelEvent.Post event) {
        if (travelSnapshot != null) {
            mc.player.setYRot(travelSnapshot.yRot());
            mc.player.setXRot(travelSnapshot.xRot());
            travelSnapshot = null;
        }
    }

    @Subscribe
    public void onUpdateWalking(UpdateWalkingPlayerEvent event) {
        if (event.getStage() == Stage.PRE) {
            if (callbacks.isEmpty()) {
                updateRenderSnapshot(new Angles(mc.player.getYRot(), mc.player.getXRot()), false);
                return;
            }
            Callback highest = callbacks.stream().max(Comparator.comparing(Callback::priority)).orElseThrow();

            updateRenderSnapshot(highest.angles(), true);

            snapshot = new Angles(mc.player.getYRot(), mc.player.getXRot());
            mc.player.setYRot(highest.angles().yRot());
            mc.player.setXRot(highest.angles().xRot());
        } else if (snapshot != null) {
            executeCallbacks(callback -> callback.type() == Type.MOTION);

            mc.player.setYRot(snapshot.yRot());
            mc.player.setXRot(snapshot.xRot());
            snapshot = null;
        }
    }

    public void sync(Angles angles, Runnable runnable) {
        sync(angles, 0, runnable);
    }

    public void sync(Angles angles, int priority, Runnable runnable) {
        request(Type.SYNC, angles, priority, runnable);
    }

    public void motion(Angles angles, Runnable runnable) {
        motion(angles, 0, runnable);
    }

    public void motion(Angles angles, int priority, Runnable runnable) {
        request(Type.MOTION, angles, priority, runnable);
    }

    public void silent(Angles angles, Runnable runnable) {
        request(Type.SILENT, angles, 0, runnable);
    }

    public Angles getLerpRenderSnapshot(float lerp) {
        if (!updateRender) return null;
        return new Angles(
                Mth.lerp(lerp, renderSnapshot0.yRot(), renderSnapshot.yRot()),
                Mth.lerp(lerp, renderSnapshot0.xRot(), renderSnapshot.xRot())
        );
    }

    public void sendDuplicate(float yaw, float pitch) {
        mc.player.positionReminder = 20;
        mc.player.yRotLast = yaw;
        mc.player.xRotLast = pitch;
        mc.player.connection.send(new ServerboundMovePlayerPacket.PosRot(
                mc.player.getX(), mc.player.getY(), mc.player.getZ(),
                yaw, pitch,
                mc.player.onGround(), mc.player.horizontalCollision
        ));
    }

    private void request(Type type, Angles angles, int priority, Runnable runnable) {
        Callback callback = new Callback(type, angles, priority, runnable);
        if (type == Type.SILENT) {
            sendDuplicate(angles.yRot(), angles.xRot());
            callback.execute();
        } else {
            callbacks.add(callback);
        }
    }

    private void executeCallbacks(Predicate<Callback> filter) {
        callbacks.removeIf(callback -> {
            if (filter.test(callback)) {
                callback.execute();
                return true;
            }
            return false;
        });
    }

    private void updateRenderSnapshot(Angles angles, boolean update) {
        this.renderSnapshot0 = this.renderSnapshot;
        this.renderSnapshot = angles;
        this.updateRender = update;

        if (renderSnapshot0 == null || renderSnapshot == null)
            return;

        if (Mth.abs(renderSnapshot.yRot() - renderSnapshot0.yRot()) > 320) {
            this.renderSnapshot0 = this.renderSnapshot;
        }
    }

    record Callback(Type type, Angles angles, int priority, Runnable action) {
        public void execute() {
            if (action != null) action.run();
        }
    }

    public enum Type {
        MOTION,
        SYNC,
        SILENT
    }
}
