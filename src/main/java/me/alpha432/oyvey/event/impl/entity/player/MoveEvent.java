package me.alpha432.oyvey.event.impl.entity.player;

import me.alpha432.oyvey.event.Event;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.phys.Vec3;

public class MoveEvent extends Event {
    private final MoverType type;
    private Vec3 movement;

    public MoveEvent(MoverType type, Vec3 movement) {
        this.type = type;
        this.movement = movement;
    }

    public MoverType getType() {
        return type;
    }

    public Vec3 getMovement() {
        return movement;
    }

    public void setMovement(Vec3 movement) {
        this.movement = movement;
    }

    public void setMovement(double x, double y, double z) {
        this.movement = new Vec3(x, y, z);
    }
}
