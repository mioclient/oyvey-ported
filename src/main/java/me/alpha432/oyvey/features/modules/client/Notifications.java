package me.alpha432.oyvey.features.modules.client;

import me.alpha432.oyvey.event.impl.ClientEvent;
import me.alpha432.oyvey.event.system.Subscribe;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.settings.Setting;
import me.alpha432.oyvey.util.TextUtil;
import me.alpha432.oyvey.util.player.ChatUtil;

public class Notifications extends Module {
    private static final String MODULE_FORMAT = "Toggled %s %s %s";

    public Setting<Boolean> moduleToggle = bool("Module Toggle", true);

    public Notifications() {
        super("Notifications", "Displays notifications for various client events", Category.CLIENT);
    }

    @Subscribe
    public void onClient(ClientEvent event) {
        if (!moduleToggle.getValue() || event.getType() != ClientEvent.Type.TOGGLE_MODULE) {
            return;
        }

        if (event.getFeature() instanceof ClickGui) {
            return;
        }

        boolean moduleState = event.getFeature().isEnabled();
        ChatUtil.sendMessage(TextUtil.text(MODULE_FORMAT,
                event.getFeature().getName(),
                moduleState ? "{green}" : "{red}",
                moduleState ? "on" : "off"));
    }
}
