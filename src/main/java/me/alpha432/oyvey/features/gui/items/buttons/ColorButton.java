package me.alpha432.oyvey.features.gui.items.buttons;

import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.features.gui.OyVeyGui;
import me.alpha432.oyvey.features.gui.Widget;
import me.alpha432.oyvey.features.modules.client.ClickGui;
import me.alpha432.oyvey.features.settings.Setting;
import me.alpha432.oyvey.util.render.RenderUtil;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;

import java.awt.*;

public class ColorButton extends Button {
    private final Setting<Color> setting;
    private boolean open = false;
    private boolean hoveringHue = false, hoveringColor = false, hoveringAlpha = false, hoveringCopy = false, hoveringPaste = false;
    private boolean draggingHue = false, draggingColor = false, draggingAlpha = false;
    private int pickerSize = 84;
    private float[] hsb;

    public ColorButton(Setting<Color> setting) {
        super(setting.getName());
        this.setting = setting;
        this.width = 15;
        hsb = Color.RGBtoHSB(setting.getValue().getRed(), setting.getValue().getGreen(), setting.getValue().getBlue(), null);
    }

    @Override
    public void drawScreen(GuiGraphics context, int mouseX, int mouseY, float partialTicks) {
        Color currentColor = setting.getValue();
        Color outlineColor = Color.BLACK;
        Color realColor = Color.getHSBColor(hsb[0], 1, 1);

        RenderUtil.rect(context, this.x, this.y, this.x + (float) this.width + 7.4f, this.y + (float) this.height - 0.5f,
                !this.isHovering(mouseX, mouseY) ? 0x11555555 : -2007673515);

        RenderUtil.rect(context, this.x + (float) this.width - 4f, this.y + 3f,
                this.x + (float) this.width + 5.0f, this.y + (float) this.height - 2.5f,
                new Color(currentColor.getRGB(), false).getRGB());

        drawString(this.getName(), this.x + 2.3f, this.y - 1.7f - (float) OyVeyGui.getClickGui().getTextOffset(), -1);

        if (open) {
            float yOffset = this.height + 1;
            int availableWidth = this.width + 3;
            int pickerWidth = Math.min(pickerSize, availableWidth);
            float pickerX = this.x + 2.0f;

            int dragX = Mth.clamp(mouseX - (int)pickerX, 0, pickerWidth);
            int dragY = Mth.clamp(mouseY - (int)(getY() + yOffset), 0, pickerWidth);
            float dragHue = Math.max(pickerWidth * hsb[0] - .5f, 1);
            float dragSaturation = Math.max(pickerWidth * hsb[1] - 1, 2);
            float dragBrightness = Math.max(pickerWidth * (1.0f - hsb[2]) - 1, 2);
            float dragAlpha = Math.max(pickerWidth * (currentColor.getAlpha() / 255.0f) - .5f, 1);

            RenderUtil.horizontalGradient(context, pickerX, this.y + yOffset, pickerX + pickerWidth, this.y + yOffset + pickerWidth, Color.WHITE, realColor);
            RenderUtil.verticalGradient(context, pickerX, this.y + yOffset, pickerX + pickerWidth, this.y + yOffset + pickerWidth, new Color(0, 0, 0, 0), Color.BLACK);
            RenderUtil.rect(context, pickerX, this.y + yOffset, pickerX + pickerWidth, this.y + yOffset + pickerWidth, outlineColor.getRGB(), 1.0f);

            hoveringColor = isHoveringArea(mouseX, mouseY, pickerX, this.y + yOffset, pickerX + pickerWidth, this.y + yOffset + pickerWidth);

            if (dragSaturation < pickerWidth && dragBrightness < pickerWidth) {
                RenderUtil.rect(context, pickerX + dragSaturation - 2.5f, this.y + yOffset + dragBrightness - 2.5f,
                        pickerX + dragSaturation + 0.5f, this.y + yOffset + dragBrightness + 0.5f, outlineColor.getRGB());
                RenderUtil.rect(context, pickerX + dragSaturation - 1.5f, this.y + yOffset + dragBrightness - 1.5f,
                        pickerX + dragSaturation - 0.5f, this.y + yOffset + dragBrightness - 0.5f, Color.WHITE.getRGB());
            }

            if (draggingColor) {
                hsb[1] = (float) dragX / pickerWidth;
                hsb[2] = 1.0f - (float) dragY / pickerWidth;
                setColor(hsb);
            }

            yOffset += pickerWidth + 2;

            RenderUtil.horizontalGradient(context, pickerX, this.y + yOffset, pickerX + pickerWidth, this.y + yOffset + 8,
                    new Color(currentColor.getRed(), currentColor.getGreen(), currentColor.getBlue(), 0),
                    new Color(currentColor.getRed(), currentColor.getGreen(), currentColor.getBlue(), 255));
            RenderUtil.rect(context, pickerX, this.y + yOffset, pickerX + pickerWidth, this.y + yOffset + 8, outlineColor.getRGB(), 1.0f);
            hoveringAlpha = isHoveringArea(mouseX, mouseY, pickerX, this.y + yOffset, pickerX + pickerWidth, this.y + yOffset + 8);

            RenderUtil.rect(context, pickerX + dragAlpha - 1.5f, this.y + yOffset - 1,
                    pickerX + dragAlpha + 1.5f, this.y + yOffset + 9, outlineColor.getRGB());
            RenderUtil.rect(context, pickerX + dragAlpha - 0.5f, this.y + yOffset,
                    pickerX + dragAlpha + 0.5f, this.y + yOffset + 8, Color.WHITE.getRGB());

            if (draggingAlpha) {
                setColor(hsb, (int) (255 * (float) dragX / pickerWidth));
            }

            yOffset += 10;

            for (float i = 0; i < pickerWidth; i += 0.5f) {
                RenderUtil.rect(context, pickerX + i, this.y + yOffset, pickerX + i + 0.5f, this.y + yOffset + 8,
                        Color.getHSBColor(i / pickerWidth, 1.0f, 1.0f).getRGB());
            }
            RenderUtil.rect(context, pickerX, this.y + yOffset, pickerX + pickerWidth, this.y + yOffset + 8, outlineColor.getRGB(), 1.0f);
            hoveringHue = isHoveringArea(mouseX, mouseY, pickerX, this.y + yOffset, pickerX + pickerWidth, this.y + yOffset + 8);

            if (dragHue < pickerWidth) {
                RenderUtil.rect(context, pickerX + dragHue - 1.5f, this.y + yOffset - 1,
                        pickerX + dragHue + 1.5f, this.y + yOffset + 9, outlineColor.getRGB());
                RenderUtil.rect(context, pickerX + dragHue - 0.5f, this.y + yOffset,
                        pickerX + dragHue + 0.5f, this.y + yOffset + 8, Color.WHITE.getRGB());
            }

            if (draggingHue) {
                hsb[0] = (float) dragX / pickerWidth;
                setColor(hsb);
            }

            yOffset += 10;

            int buttonWidth = availableWidth / 2;
            RenderUtil.rect(context, pickerX, this.y + yOffset, pickerX + buttonWidth, this.y + yOffset + 14,
                    hoveringCopy ? OyVey.colorManager.getColorWithAlpha(y, ClickGui.getInstance().topColor.getValue().getAlpha()) : 0x11555555);
            drawString("Copy", pickerX + buttonWidth / 2 - mc.font.width("Copy") / 2, this.y + yOffset + 3, -1);
            hoveringCopy = isHoveringArea(mouseX, mouseY, pickerX, this.y + yOffset, pickerX + buttonWidth, this.y + yOffset + 14);

            RenderUtil.rect(context, pickerX + buttonWidth + 1, this.y + yOffset, pickerX + buttonWidth * 2 + 1, this.y + yOffset + 14,
                    hoveringPaste ? OyVey.colorManager.getColorWithAlpha(y, ClickGui.getInstance().topColor.getValue().getAlpha()) : 0x11555555);
            drawString("Paste", pickerX + buttonWidth + buttonWidth / 2 - mc.font.width("Paste") / 2 + 1, this.y + yOffset + 3, -1);
            hoveringPaste = isHoveringArea(mouseX, mouseY, pickerX + buttonWidth + 1, this.y + yOffset, pickerX + buttonWidth * 2 + 1, this.y + yOffset + 14);
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (this.isHovering(mouseX, mouseY) && mouseButton == 1) {
            open = !open;
            mc.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1f));
        }

        if (mouseButton == 0) {
            if (hoveringHue) draggingHue = true;
            if (hoveringColor) draggingColor = true;
            if (hoveringAlpha) draggingAlpha = true;

            if (hoveringCopy) {
                OyVeyGui.setColorClipboard(setting.getValue());
                mc.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1f));
            }
            if (hoveringPaste && OyVeyGui.getColorClipboard() != null) {
                setting.setValue(OyVeyGui.getColorClipboard());
                hsb = Color.RGBtoHSB(setting.getValue().getRed(), setting.getValue().getGreen(), setting.getValue().getBlue(), null);
                mc.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1f));
            }
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int releaseButton) {
        if (releaseButton == 0) {
            draggingHue = false;
            draggingColor = false;
            draggingAlpha = false;
        }
    }

    @Override
    public void update() {
        this.setHidden(!this.setting.isVisible());
    }

    @Override
    public int getHeight() {
        if (!open) return 14;
        int pickerWidth = Math.min(pickerSize, this.width + 3);
        return 14 + pickerWidth + 8 + 8 + 14 + 8;
    }

    @Override
    public boolean isHovering(int mouseX, int mouseY) {
        for (Widget widget : OyVeyGui.getClickGui().getComponents()) {
            if (widget.drag) return false;
        }
        return (float) mouseX >= this.getX() && (float) mouseX <= this.getX() + (float) this.getWidth() + 8.0f
                && (float) mouseY >= this.getY() && (float) mouseY < this.getY() + (float) this.height;
    }

    private boolean isHoveringArea(int mouseX, int mouseY, float left, float top, float right, float bottom) {
        for (Widget widget : OyVeyGui.getClickGui().getComponents()) {
            if (widget.drag) return false;
        }
        return left <= mouseX && top <= mouseY && right > mouseX && bottom > mouseY;
    }

    private void setColor(float[] hsb) {
        setColor(hsb, setting.getValue().getAlpha());
    }

    private void setColor(float[] hsb, int alpha) {
        Color color = new Color(Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]));
        setting.setValue(new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha));
    }
}
