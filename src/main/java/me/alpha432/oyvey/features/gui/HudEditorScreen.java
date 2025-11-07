package me.alpha432.oyvey.features.gui;

import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.features.Feature;
import me.alpha432.oyvey.features.gui.items.buttons.ModuleButton;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.modules.client.HudModule;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.Comparator;

public class HudEditorScreen extends Screen {
    private final ArrayList<Component> components = new ArrayList<>();
    public HudModule currentDragging;
    public boolean anyHover;

    public HudEditorScreen() {
        super(Text.literal("oyvey-hudeditor"));
        load();
    }

    private void load() {
        Component hud = new Component("Hud", 50, 50, true);
        OyVey.moduleManager.stream()
                .filter(m -> m.getCategory() == Module.Category.HUD && !m.hidden)
                .map(ModuleButton::new)
                .forEach(hud::addButton);
        this.components.forEach(component -> component.getItems().sort(Comparator.comparing(Feature::getName)));
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        anyHover = false;
        this.components.forEach(component -> component.drawScreen(context, mouseX, mouseY, delta));
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int clickedButton) {
        this.components.forEach(component -> component.mouseClicked((int) mouseX, (int) mouseY, clickedButton));
        return super.mouseClicked(mouseX, mouseY, clickedButton);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int releaseButton) {
        this.components.forEach(component -> component.mouseReleased((int) mouseX, (int) mouseY, releaseButton));
        return super.mouseReleased(mouseX, mouseY, releaseButton);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        if (verticalAmount < 0) {
            this.components.forEach(component -> component.setY(component.getY() - 10));
        } else if (verticalAmount > 0) {
            this.components.forEach(component -> component.setY(component.getY() + 10));
        }
        return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        this.components.forEach(component -> component.onKeyPressed(keyCode));
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean charTyped(char chr, int modifiers) {
        this.components.forEach(component -> component.onKeyTyped(chr, modifiers));
        return super.charTyped(chr, modifiers);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    @Override // ignore 1.21.8 menu blur thing
    public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {
    }


    public ArrayList<Component> getComponents() {
        return components;
    }
}

