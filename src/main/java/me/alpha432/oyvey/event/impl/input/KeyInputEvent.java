package me.alpha432.oyvey.event.impl.input;

import me.alpha432.oyvey.event.Event;

public class KeyInputEvent extends Event {
    private final int key;

    public KeyInputEvent(int key) {
        this.key = key;
    }

    public int getKey() {
        return key;
    }
}
