package me.alpha432.oyvey.features.modules.client;

import me.alpha432.oyvey.event.impl.ClientEvent;
import me.alpha432.oyvey.event.system.Subscribe;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.util.TextUtil;
import me.alpha432.oyvey.util.player.ChatUtil;

public class Notifications extends Module {
    private static final String MODULE_FORMAT = "{gray} %s.enabled = {} %s";

    public Notifications() {
        super("Notifications", "Settings for notifications", Category.CLIENT);
    }

    @Subscribe
    public void onClient(ClientEvent event) {
        if (event.getStage() == 2) return;
        if (event.getFeature() instanceof ClickGui) return;

        var name = event.getFeature().getName();
        var state = event.getStage() != 0;
        var message = TextUtil.getFormatter().format(MODULE_FORMAT, name, state);
        ChatUtil.sendMessage(message);
    }
}
