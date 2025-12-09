package me.alpha432.oyvey.event.impl;

import com.mojang.blaze3d.vertex.PoseStack;
import me.alpha432.oyvey.event.Event;

public class Render3DEvent extends Event {
    private final PoseStack matrix;
    private final float delta;

    public Render3DEvent(PoseStack matrix, float delta) {
        this.matrix = matrix;
        this.delta = delta;
    }

    public PoseStack getMatrix() {
        return matrix;
    }

    public float getDelta() {
        return delta;
    }
}
