package me.alpha432.oyvey.features.modules.hud;

import com.google.common.eventbus.Subscribe;
import me.alpha432.oyvey.event.impl.Render2DEvent;
import me.alpha432.oyvey.features.modules.client.HudModule;
import me.alpha432.oyvey.features.settings.Setting;
import me.alpha432.oyvey.util.TextUtil;
import net.minecraft.client.MinecraftClient;

public class Welcomer extends HudModule {

    public Setting<String> text = str("Text", "Welcome");

    public Welcomer() {
        super("Welcomer", "Display a welcome message", 100, 30);
    }

    @Override
    @Subscribe
    public void onRender2D(Render2DEvent e) {
        super.onRender2DHud(e);

        String playerName = MinecraftClient.getInstance().getSession().getUsername();

        e.getContext().drawTextWithShadow(mc.textRenderer,
                TextUtil.text("{global} %s {} %s", text.getValue(), playerName),
                (int) getX(), (int) getY(), -1);

        String welcomeString = text.getValue() + " " + playerName;
        setWidth(mc.textRenderer.getWidth(welcomeString));
        setHeight(mc.textRenderer.fontHeight);
    }
}