package me.alpha432.oyvey.features.modules.client;

import com.google.common.eventbus.Subscribe;
import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.event.impl.Render2DEvent;
import me.alpha432.oyvey.features.settings.Setting;
import me.alpha432.oyvey.util.TextUtil;

public class Watermark extends HudModule {
    public Setting<String> text = str("Text", "OyVey");

    public Watermark() {
        super("Watermark", "Display watermark", 100, 10);
    }

    @Override
    @Subscribe
    public void onRender2D(Render2DEvent e) {
        super.onRender2DHud(e);

        e.getContext().drawTextWithShadow(mc.textRenderer,
                TextUtil.text("{global} %s {} %s", text.getValue(), OyVey.VERSION),
                (int) getX(), (int) getY(), -1);

        String watermarkString = text.getValue() + " " + OyVey.VERSION;
        setWidth(mc.textRenderer.getWidth(watermarkString));
        setHeight(mc.textRenderer.fontHeight);
    }
}

