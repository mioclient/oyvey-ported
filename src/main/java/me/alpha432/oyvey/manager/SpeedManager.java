package me.alpha432.oyvey.manager;

import me.alpha432.oyvey.features.Feature;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

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

    public double getSpeed(Player player) {
        if (mc.player == player) return localSpeedNormal;
        return getCurrentSpeed(player);
    }

    public double getSpeedBpS(Player player) {
        return getSpeed(player) * 20;
    }

    public double getSpeedKmH(Player player) {
        return getSpeedBpS(player) * 3.6;
    }

    public float getCurrentSpeed(Entity entity) {
        Entity vehicle = entity.getVehicle();
        double distTraveledX = entity.getX() - entity.xo;
        double distTraveledZ = entity.getZ() - entity.zo;
        if (vehicle != null) {
            distTraveledX = vehicle.getX() - vehicle.xo;
            distTraveledZ = vehicle.getZ() - vehicle.zo;
        }

        return  (float) Math.hypot(distTraveledX, distTraveledZ);
    }
}