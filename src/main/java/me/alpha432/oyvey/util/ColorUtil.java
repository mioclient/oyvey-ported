package me.alpha432.oyvey.util;

import me.alpha432.oyvey.features.modules.client.ClickGuiModule;

import java.awt.*;

public class ColorUtil {
    public static Color rainbow(int delay) {
        double rainbowState = Math.ceil((double) (System.currentTimeMillis() + (long) delay) / 20.0);
        return Color.getHSBColor((float) ((rainbowState % 360.0) / 360.0), ClickGuiModule.getInstance().rainbowSaturation.getValue() / 255.0f, ClickGuiModule.getInstance().rainbowBrightness.getValue() / 255.0f);
    }
}