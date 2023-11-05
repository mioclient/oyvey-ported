package me.alpha432.oyvey.event.impl;

import me.alpha432.oyvey.event.Event;
import me.alpha432.oyvey.event.Stage;

public class UpdateWalkingPlayerEvent extends Event {
    private final Stage stage;

    public UpdateWalkingPlayerEvent(Stage stage) {
        this.stage = stage;
    }

    public Stage getStage() {
        return stage;
    }
}
