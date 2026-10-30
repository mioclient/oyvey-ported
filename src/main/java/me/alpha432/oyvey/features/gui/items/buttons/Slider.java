package me.alpha432.oyvey.features.gui.items.buttons;

import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.features.gui.OyVeyGui;
import me.alpha432.oyvey.features.gui.Widget;
import me.alpha432.oyvey.features.modules.client.ClickGuiModule;
import me.alpha432.oyvey.features.settings.Setting;
import me.alpha432.oyvey.util.render.RenderUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import org.lwjgl.glfw.GLFW;

public class Slider
        extends Button {
    private final Number min;
    private final Number max;
    private final double difference;

    public Setting<Number> setting;

    private boolean isListening = false;
    private String input = "";

    public Slider(Setting<Number> setting) {
        super(setting.getName());
        this.setting = setting;
        this.min = setting.getMin();
        this.max = setting.getMax();
        this.difference = max.doubleValue() - min.doubleValue();
        this.width = 15;
    }

    @Override
    public void drawScreen(GuiGraphics context, int mouseX, int mouseY, float partialTicks) {
        this.dragSetting(mouseX, mouseY);

        RenderUtil.rect(context, this.x, this.y, this.x + this.width + 7.4f, this.y + this.height - 0.5f, !this.isHovering(mouseX, mouseY) ? 0x11555555 : -2007673515);

        RenderUtil.rect(context, this.x, this.y,
                this.x + (this.width + 7.4f) * this.partialMultiplier(),
                this.y + this.height - 0.5f,
                !this.isHovering(mouseX, mouseY) ? OyVey.colorManager.getColorWithAlpha(y, ClickGuiModule.getInstance().color.getValue().getAlpha())
                        : OyVey.colorManager.getColorWithAlpha(y, ClickGuiModule.getInstance().topColor.getValue().getAlpha()
                )
        );

        String value = isListening ? input + "_" : String.valueOf(setting.getValue());

        drawString(context, this.getName() + " " + ChatFormatting.GRAY + value, this.x + 2.3f, this.y - 1.7f - OyVeyGui.getClickGui().getTextOffset(), -1);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (this.isHovering(mouseX, mouseY) && mouseButton == GLFW.GLFW_MOUSE_BUTTON_RIGHT) {
            isListening = true;
            input = String.valueOf(setting.getValue());
            return;
        }

        if (isListening) return;

        super.mouseClicked(mouseX, mouseY, mouseButton);

        if (this.isHovering(mouseX, mouseY)
                && mouseButton == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
            this.setSettingFromX(mouseX);
        }
    }

    @Override
    public void onKeyTyped(String typedChar, int keyCode) {
        if (!isListening) return;

        for (char c : typedChar.toCharArray()) {
            if (Character.isDigit(c)) {
                input += c;
            } else if (c == '-' && input.isEmpty() && min.doubleValue() < 0) {
                input += c;
            } else if (c == '.' && !(setting.getValue() instanceof Integer) && !input.contains(".")) {
                input += c;
            }
        }
    }

    @Override
    public void onKeyPressed(int key) {
        if (!isListening) return;

        switch (key) {
            case GLFW.GLFW_KEY_ENTER:
            case GLFW.GLFW_KEY_KP_ENTER:
                enterNumber();
                break;

            case GLFW.GLFW_KEY_BACKSPACE:
                if (!input.isEmpty()) {
                    input = input.substring(0, input.length() - 1);
                }
                break;

            case GLFW.GLFW_KEY_ESCAPE:
                isListening = false;
                input = "";
                break;
        }
    }

    private void enterNumber() {
        try {
            if (input.isEmpty() || input.equals("-") || input.equals(".")) {
                return;
            }

            if (setting.getValue() instanceof Integer) {
                int value = Integer.parseInt(input);
                value = Math.max(min.intValue(), Math.min(max.intValue(), value));
                setting.setValue(value);

            } else if (setting.getValue() instanceof Float) {
                float value = Float.parseFloat(input);

                if (!Float.isFinite(value)) return;

                value = Math.max(min.floatValue(), Math.min(max.floatValue(), value));

                setting.setValue(value);

            } else {
                double value = Double.parseDouble(input);

                if (!Double.isFinite(value)) return;

                value = Math.max(min.doubleValue(), Math.min(max.doubleValue(), value));

                setting.setValue(value);
            }

            isListening = false;
            input = "";

        } catch (NumberFormatException ignored) {
            // Keep listening so the input can be corrected.
        }
    }

    @Override
    public boolean isHovering(int mouseX, int mouseY) {
        for (Widget widget : OyVeyGui.getClickGui().getComponents()) {
            if (widget.drag) return false;
        }

        return mouseX >= this.getX()
                && mouseX <= this.getX() + this.getWidth() + 8
                && mouseY >= this.getY()
                && mouseY < this.getY() + this.height;
    }

    @Override
    public void update() {
        this.setHidden(!this.setting.isVisible());
    }

    private void dragSetting(int mouseX, int mouseY) {
        if (!isListening
                && this.isHovering(mouseX, mouseY)
                && GLFW.glfwGetMouseButton(
                mc.getWindow().handle(),
                GLFW.GLFW_MOUSE_BUTTON_LEFT
        ) == 1) {
            this.setSettingFromX(mouseX);
        }
    }

    private void setSettingFromX(int mouseX) {
        double percent = Math.clamp(((double) mouseX - this.x) / (this.width + 7.4), 0.0,1.0);

        double result = min.doubleValue() + difference * percent;

        if (setting.getValue() instanceof Double) {
            setting.setValue(Math.round(result * 10.0) / 10.0);
        } else if (setting.getValue() instanceof Float) {
            setting.setValue((float) (Math.round(result * 10.0) / 10.0));
        } else if (setting.getValue() instanceof Integer) {
            setting.setValue((int) Math.round(result));
        }
    }

    private float partialMultiplier() {
        if (difference == 0) return 0;

        return (float) Math.clamp((setting.getValue().doubleValue() - min.doubleValue()) / difference, 0.0, 1.0);
    }
}