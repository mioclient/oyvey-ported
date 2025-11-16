package me.alpha432.oyvey.features.modules.client;

import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.event.impl.MouseEvent;
import me.alpha432.oyvey.event.impl.Render2DEvent;
import me.alpha432.oyvey.event.system.Subscribe;
import me.alpha432.oyvey.features.gui.HudEditorScreen;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.settings.Setting;
import me.alpha432.oyvey.util.render.RenderUtil;
import net.minecraft.client.gui.screens.ChatScreen;
import org.joml.Vector2f;

public abstract class HudModule extends Module {
    public final Setting<Vector2f> pos = vec2f("Position", 0.5f, 0.5f);

    private float dragX, dragY, width, height;
    private boolean dragging, button;

    public HudModule(String name, String description, float width, float height) {
        super(name, description, Category.HUD);
        this.width = width;
        this.height = height;
    }

    public float getX() {
        return mc.getWindow().getGuiScaledWidth() * pos.getValue().x();
    }

    public float getY() {
        float heightWithChat = mc.getWindow().getGuiScaledHeight() - 14;
        float baseY = mc.getWindow().getGuiScaledHeight() * pos.getValue().y();
        float combined = baseY + getHeight();

        if (mc.screen instanceof ChatScreen) {
            baseY = Math.min(combined, heightWithChat) - getHeight();
        }
        return baseY;
    }

    @Subscribe
    public void onRender2DHud(Render2DEvent e) {
        render(e);
    }

    @Subscribe
    public void onMouse(MouseEvent e) {
        if (!(mc.screen instanceof HudEditorScreen) || nullCheck()) return;
        if (OyVey.hudEditorScreen == null) return;

        if (e.getAction() == 0) {
            button = false;
            dragging = false;
            OyVey.hudEditorScreen.currentDragging = null;
        }

        if (e.getAction() == 1 && isHovering()) {
            button = true;
        }
    }

    protected void render(Render2DEvent e) {
        if (!(mc.screen instanceof HudEditorScreen) || nullCheck() || OyVey.hudEditorScreen == null) return;

        float x = getX();
        float y = getY();

        if (button) {
            if (!dragging && isHovering() && OyVey.hudEditorScreen.currentDragging == null) {
                dragX = getMouseX() - x;
                dragY = getMouseY() - y;
                dragging = true;
                OyVey.hudEditorScreen.currentDragging = this;
            }

            if (dragging) {
                float finalX = Math.min(Math.max(getMouseX() - dragX, 0),
                        mc.getWindow().getGuiScaledWidth() - width);
                float finalY = Math.min(Math.max(getMouseY() - dragY, 0),
                        mc.getWindow().getGuiScaledHeight() - height);

                pos.getValue().x = finalX / mc.getWindow().getGuiScaledWidth();
                pos.getValue().y = finalY / mc.getWindow().getGuiScaledHeight();
            }
        } else {
            dragging = false;
        }

        boolean shouldDrawDescription = isHovering() && !OyVey.hudEditorScreen.anyHover;
        if (OyVey.hudEditorScreen.currentDragging != null) {
            shouldDrawDescription = OyVey.hudEditorScreen.currentDragging == this;
        }

        if (shouldDrawDescription) {
            int textWidth = mc.font.width(getName());
            int textHeight = mc.font.lineHeight;
            float textX = x + width + 5;
            if (textX + textWidth > mc.getWindow().getGuiScaledWidth()) {
                textX = x - 5 - textWidth;
            }
            e.getContext().drawString(mc.font, getName(),
                    (int) textX, (int) (y + height / 2f - textHeight / 2f), -1);
            OyVey.hudEditorScreen.anyHover = true;
        }

        RenderUtil.rect(e.getContext(),
                x - 1, y - 1, x + width + 1, y + height + 1,
                OyVey.colorManager.getColor().getRGB(), 1.0f);
    }

    public int getMouseX() {
        return (int) (mc.mouseHandler.xpos() / mc.getWindow().getGuiScale());
    }

    public int getMouseY() {
        return (int) (mc.mouseHandler.ypos() / mc.getWindow().getGuiScale());
    }

    public void setBounds(float x, float y, float width, float height) {
        this.width = width;
        this.height = height;
        pos.getValue().x = x / mc.getWindow().getGuiScaledWidth();
        pos.getValue().y = y / mc.getWindow().getGuiScaledHeight();
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
