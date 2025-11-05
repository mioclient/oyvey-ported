package me.alpha432.oyvey.manager;

import me.alpha432.oyvey.features.Feature;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;

import java.util.ArrayList;
import java.util.List;

public class SpeedManager extends Feature {
    private static final int SPEED_NORMALIZATION = 20;

    private final List<Float> localSpeed = new ArrayList<>();
    private float localSpeedNormal = 0f;

    public void update() {
        localSpeed.add(getCurrentSpeed(mc.player));
        while (localSpeed.size() > SPEED_NORMALIZATION) localSpeed.removeFirst();

        localSpeedNormal = localSpeed.stream().reduce(0f, Float::sum) / localSpeed.size();
    }

    public double getSpeed(PlayerEntity player) {
        if (mc.player == player) return localSpeedNormal;
        return getCurrentSpeed(player);
    }

    public double getSpeedBpS(PlayerEntity player) {
        return getSpeed(player) * 20;
    }

    public double getSpeedKmH(PlayerEntity player) {
        return getSpeedBpS(player) * 3.6;
    }

    public float getCurrentSpeed(Entity entity) {
        Entity vehicle = entity.getVehicle();
        double distTraveledX = entity.getX() - entity.lastX;
        double distTraveledZ = entity.getZ() - entity.lastZ;
        if (vehicle != null) {
            distTraveledX = vehicle.getX() - vehicle.lastX;
            distTraveledZ = vehicle.getZ() - vehicle.lastZ;
        }

        return  (float) Math.hypot(distTraveledX, distTraveledZ);
    }
}