package me.alpha432.oyvey.event.impl.entity.player;

import me.alpha432.oyvey.event.Event;

public class TickEvent extends Event {
    public static class Post extends TickEvent { }

    public static class Pre extends TickEvent { }
}
