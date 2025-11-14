package me.alpha432.oyvey.util.traits;

import me.alpha432.oyvey.event.system.EventBus;
import net.minecraft.client.MinecraftClient;

public interface Util {
    MinecraftClient mc = MinecraftClient.getInstance();
    EventBus EVENT_BUS = new EventBus();
}
