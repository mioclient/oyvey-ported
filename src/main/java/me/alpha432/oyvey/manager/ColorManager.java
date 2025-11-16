package me.alpha432.oyvey.manager;

import me.alpha432.oyvey.features.modules.client.ClickGui;
import me.alpha432.oyvey.util.ColorUtil;

import java.awt.*;

public class ColorManager {
    private Color color = new Color(0, 0, 255, 180);

    public void init() {
        ClickGui ui = ClickGui.getInstance();
        setColor(ui.color.getValue());
    }

    public Color getColor() {
        return this.color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public int getColorAsInt() {
        return ColorUtil.toRGBA(this.color);
    }

    public int getColorAsIntFullAlpha() {
        return ColorUtil.toRGBA(new Color(this.color.getRed(), this.color.getGreen(), this.color.getBlue(), 255));
    }

    public int getColorWithAlpha(float offset, int alpha) {
        if (ClickGui.getInstance().rainbow.getValue()) {
            return ColorUtil.rainbow((int) (offset / 10f * ClickGui.getInstance().rainbowHue.getValue())).getRGB();
        }
        return new Color(this.color.getRed(), this.color.getGreen(), this.color.getBlue(), alpha).getRGB();
    }
}
