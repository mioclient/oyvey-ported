package me.alpha432.oyvey.features.modules.client;

import com.google.common.eventbus.Subscribe;
import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.event.impl.UpdateEvent;
import me.alpha432.oyvey.features.gui.HudEditorScreen;
import me.alpha432.oyvey.features.modules.Module;

public class HudEditor extends Module {
    public HudEditor() {
        super("HudEditor", "Edit HUD element positions", Category.CLIENT, true, false, false);
    }

    @Override
    public void onEnable() {
        if (fullNullCheck()) {
            disable();
            return;
        }
        mc.setScreen(OyVey.hudEditorScreen);
    }

    @Override
    public void onDisable() {
        if (fullNullCheck()) return;
        mc.setScreen(null);
    }

    @Subscribe
    public void onUpdate(UpdateEvent event) {
        if (fullNullCheck()) return;
        if (!(mc.currentScreen instanceof HudEditorScreen)) {
            disable();
        }
    }
}

