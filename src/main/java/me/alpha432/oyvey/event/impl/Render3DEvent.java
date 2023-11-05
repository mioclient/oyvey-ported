package me.alpha432.oyvey.event.impl;

import me.alpha432.oyvey.event.Event;
import net.minecraft.client.util.math.MatrixStack;

public class Render3DEvent extends Event {
    private final MatrixStack matrix;
    private final float delta;

    public Render3DEvent(MatrixStack matrix, float delta) {
        this.matrix = matrix;
        this.delta = delta;
    }

    public MatrixStack getMatrix() {
        return matrix;
    }

    public float getDelta() {
        return delta;
    }
}
