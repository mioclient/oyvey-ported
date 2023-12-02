package me.alpha432.oyvey.manager;

import me.alpha432.oyvey.features.gui.Component;
import me.alpha432.oyvey.features.modules.client.ClickGui;
import me.alpha432.oyvey.util.ColorUtil;

import java.awt.*;

public class ColorManager {
    private float red = 1.0f;
    private float green = 1.0f;
    private float blue = 1.0f;
    private float alpha = 1.0f;
    private Color color = new Color(this.red, this.green, this.blue, this.alpha);

    public void init() {
        ClickGui ui = ClickGui.getInstance();
        setColor(ui.red.getValue(), ui.green.getValue(), ui.blue.getValue(), ui.hoverAlpha.getValue());
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

    public int getColorWithAlpha(int alpha) {
        if (ClickGui.getInstance().rainbow.getValue()) {
            return ColorUtil.rainbow(Component.counter1[0] * ClickGui.getInstance().rainbowHue.getValue()).getRGB();
        }
        return ColorUtil.toRGBA(new Color(this.red, this.green, this.blue, (float) alpha / 255.0f));
    }

    public void setColor(float red, float green, float blue, float alpha) {
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = alpha;
        this.updateColor();
    }

    public void updateColor() {
        this.setColor(new Color(this.red, this.green, this.blue, this.alpha));
    }

    public void setColor(int red, int green, int blue, int alpha) {
        this.red = (float) red / 255.0f;
        this.green = (float) green / 255.0f;
        this.blue = (float) blue / 255.0f;
        this.alpha = (float) alpha / 255.0f;
        this.updateColor();
    }

    public void setRed(float red) {
        this.red = red;
        this.updateColor();
    }

    public void setGreen(float green) {
        this.green = green;
        this.updateColor();
    }

    public void setBlue(float blue) {
        this.blue = blue;
        this.updateColor();
    }

    public void setAlpha(float alpha) {
        this.alpha = alpha;
        this.updateColor();
    }
}
