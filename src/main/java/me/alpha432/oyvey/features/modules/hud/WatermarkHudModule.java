package me.alpha432.oyvey.features.modules.hud;

import me.alpha432.oyvey.event.impl.render.Render2DEvent;
import me.alpha432.oyvey.features.modules.client.HudModule;
import me.alpha432.oyvey.features.settings.Setting;
import me.alpha432.oyvey.util.BuildConfig;
import me.alpha432.oyvey.util.TextUtil;

public class WatermarkHudModule extends HudModule {
    public Setting<String> text = str("Text", BuildConfig.NAME);
    public Setting<Boolean> fullVersion = new Setting<>("FullVersion", false);

    public WatermarkHudModule() {
        super("Watermark", "Display watermark", 100, 10);
        if (BuildConfig.USING_GIT) {
            register(fullVersion);
        }
    }

    @Override
    protected void render(Render2DEvent e) {
        super.render(e);
        String watermarkString = "{global} %s {} %s";
        if (fullVersion.getValue() && BuildConfig.USING_GIT) {
            watermarkString += "/" + BuildConfig.BRANCH + "-" + BuildConfig.HASH;
        }

        e.getContext().drawString(mc.font,
                TextUtil.text(watermarkString, text.getValue(), BuildConfig.VERSION),
                (int) getX(), (int) getY(), -1);

        setWidth(mc.font.width(watermarkString));
        setHeight(mc.font.lineHeight);
    }
}

