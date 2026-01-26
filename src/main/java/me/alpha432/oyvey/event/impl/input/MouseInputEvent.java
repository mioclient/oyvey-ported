package me.alpha432.oyvey.event.impl.input;

import me.alpha432.oyvey.event.Event;

public class MouseInputEvent extends Event {
    private final int button;
    private final int action;

    public MouseInputEvent(int button, int action) {
        this.button = button;
        this.action = action;
    }

    public int getButton() {
        return button;
    }

    public int getAction() {
        return action;
    }
}

