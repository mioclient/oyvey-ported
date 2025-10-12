package me.alpha432.oyvey.features.modules.client;

import com.google.common.eventbus.Subscribe;
import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.event.impl.MouseEvent;
import me.alpha432.oyvey.event.impl.Render2DEvent;
import me.alpha432.oyvey.features.gui.HudEditorScreen;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.settings.Pos;
import me.alpha432.oyvey.features.settings.Setting;
import me.alpha432.oyvey.util.render.RenderUtil;
import net.minecraft.client.gui.screen.ChatScreen;

public abstract class HudModule extends Module {
    public final Setting<Pos> pos = pos("Position", 0.5f, 0.5f);
    public final Setting<Boolean> chatOffset = bool("ChatOffset", true);
    private float dragX, dragY, width, height;
    private boolean dragging, button;

    public HudModule(String name, String description, float width, float height) {
        super(name, description, Category.HUD, true, false, false);
        this.width = width;
        this.height = height;
    }

    public float getX() {
        return mc.getWindow().getScaledWidth() * pos.getValue().getX();
    }

    public float getY() {
        float baseY = mc.getWindow().getScaledHeight() * pos.getValue().getY();
        if (chatOffset.getValue() && mc.currentScreen instanceof ChatScreen && !(mc.currentScreen instanceof HudEditorScreen)) {
            float chatHeight = 14f;
            if (baseY > mc.getWindow().getScaledHeight() / 2f) {
                baseY -= chatHeight;
            }
        }
        return baseY;
    }

    @Subscribe
    public void onRender2DHud(Render2DEvent e) {
        if (!(mc.currentScreen instanceof HudEditorScreen) || fullNullCheck()) return;

        float x = getX();
        float y = getY();

        if (button) {
            if (!dragging && isHovering() && (OyVey.hudEditorScreen == null || !OyVey.hudEditorScreen.anyHover)) {
                dragX = getMouseX() - x;
                dragY = getMouseY() - y;
                dragging = true;
                if (OyVey.hudEditorScreen != null) {
                    OyVey.hudEditorScreen.currentDragging = this;
                }
            }

            if (dragging) {
                float finalX = Math.min(Math.max(getMouseX() - dragX, 0),
                        mc.getWindow().getScaledWidth() - width);
                float finalY = Math.min(Math.max(getMouseY() - dragY, 0),
                        mc.getWindow().getScaledHeight() - height);

                pos.getValue().setX(finalX / mc.getWindow().getScaledWidth());
                pos.getValue().setY(finalY / mc.getWindow().getScaledHeight());
            }
        } else {
            dragging = false;
        }

        if (isHovering()) {
            int textWidth = mc.textRenderer.getWidth(getName());
            int textHeight = mc.textRenderer.fontHeight;
            float textX = x + width + 5;
            if (textX + textWidth > mc.getWindow().getScaledWidth()) {
                textX = x - 5 - textWidth;
            }
            e.getContext().drawTextWithShadow(mc.textRenderer, getName(),
                    (int) textX, (int) (y + height / 2f - textHeight / 2f), -1);
        }

        RenderUtil.rect(e.getContext().getMatrices(),
                x - 1, y - 1, x + width + 1, y + height + 1,
                OyVey.colorManager.getColor().getRGB(), 1.0f);
    }

    @Subscribe
    public void onMouse(MouseEvent e) {
        if (!(mc.currentScreen instanceof HudEditorScreen) || fullNullCheck()) return;

        if (e.getAction() == 0) {
            button = false;
            dragging = false;
            if (OyVey.hudEditorScreen != null) {
                OyVey.hudEditorScreen.currentDragging = null;
            }
        } else if (e.getAction() == 1 && isHovering() &&
                (OyVey.hudEditorScreen == null || OyVey.hudEditorScreen.currentDragging == null)) {
            button = true;
        }
    }

    public int getMouseX() {
        return (int) (mc.mouse.getX() / mc.getWindow().getScaleFactor());
    }

    public int getMouseY() {
        return (int) (mc.mouse.getY() / mc.getWindow().getScaleFactor());
    }

    public void setBounds(float x, float y, float width, float height) {
        this.width = width;
        this.height = height;
        pos.getValue().setX(x / mc.getWindow().getScaledWidth());
        pos.getValue().setY(y / mc.getWindow().getScaledHeight());
    }

    public boolean isHovering() {
        float x = getX();
        float y = getY();
        int mouseX = getMouseX();
        int mouseY = getMouseY();

        return mouseX >= x - 1 && mouseX <= x + width + 1 &&
                mouseY >= y - 1 && mouseY <= y + height + 1;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public void setHeight(float height) {
        this.height = height;
    }
}
