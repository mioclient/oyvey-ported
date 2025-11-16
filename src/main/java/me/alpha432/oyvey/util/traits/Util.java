package me.alpha432.oyvey.util.traits;

import me.alpha432.oyvey.event.system.EventBus;
import net.minecraft.client.Minecraft;

public interface Util {
    Minecraft mc = Minecraft.getInstance();
    EventBus EVENT_BUS = new EventBus();
}
