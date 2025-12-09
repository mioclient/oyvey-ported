package me.alpha432.oyvey.features.modules;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.cattyn.catformat.fabric.FabricCatFormat;
import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.event.impl.ClientEvent;
import me.alpha432.oyvey.event.impl.Render2DEvent;
import me.alpha432.oyvey.event.impl.Render3DEvent;
import me.alpha432.oyvey.features.Feature;
import me.alpha432.oyvey.features.commands.Command;
import me.alpha432.oyvey.features.settings.Bind;
import me.alpha432.oyvey.features.settings.Setting;
import me.alpha432.oyvey.manager.ConfigManager;
import me.alpha432.oyvey.util.traits.Jsonable;
import net.minecraft.ChatFormatting;
import org.joml.Vector2f;

public class Module extends Feature implements Jsonable {
    private final String description;
    private final Category category;

    public final Setting<Boolean> enabled = bool("Enabled", false);
    public final Setting<Boolean> drawn = bool("Drawn", true);
    public final Setting<Bind> bind = key("Keybind", new Bind(-1));
    public final Setting<String> displayName;

    public boolean hidden;

    public Module(String name, String description, Category category) {
        super(name);
        this.displayName = str("DisplayName", name);
        this.description = description;
        this.category = category;
    }

    public void onEnable() {
    }

    public void onDisable() {
    }

    public void onToggle() {
    }

    public void onLoad() {
    }

    public void onTick() {
    }

    public void onRender2D(Render2DEvent event) {
    }

    public void onRender3D(Render3DEvent event) {
    }

    public void onUnload() {
    }

    public String getDisplayInfo() {
        return null;
    }

    public void setEnabled(boolean enabled) {
        if (enabled) {
            this.enable();
        } else {
            this.disable();
        }
    }

    public void enable() {
        this.enabled.setValue(true);
        EVENT_BUS.register(this);
        this.onToggle();
        this.onEnable();
    }

    public void disable() {
        EVENT_BUS.unregister(this);
        this.enabled.setValue(false);
        this.onToggle();
        this.onDisable();
    }

    public void toggle() {
        ClientEvent event = new ClientEvent(!this.isEnabled() ? 1 : 0, this);
        EVENT_BUS.post(event);
        if (!event.isCancelled()) {
            this.setEnabled(!this.isEnabled());
        }
    }

    public String getDisplayName() {
        return this.displayName.getValue();
    }

    public void setDisplayName(String name) {
        Module module = OyVey.moduleManager.getModuleByDisplayName(name);
        Module originalModule = OyVey.moduleManager.getModuleByName(name);
        if (module == null && originalModule == null) {
            Command.sendMessage(this.getDisplayName() + ", name: " + this.getName() + ", has been renamed to: " + name);
            this.displayName.setValue(name);
            return;
        }
        Command.sendMessage(ChatFormatting.RED + "A module of this name already exists.");
    }

    @Override
    public boolean isEnabled() {
        return enabled.getValue();
    }

    public String getDescription() {
        return this.description;
    }

    public boolean isDrawn() {
        return this.drawn.getValue();
    }

    public void setDrawn(boolean drawn) {
        this.drawn.setValue(drawn);
    }

    public Category getCategory() {
        return this.category;
    }

    public String getInfo() {
        return null;
    }

    public Bind getBind() {
        return this.bind.getValue();
    }

    public void setBind(int key) {
        this.bind.setValue(new Bind(key));
    }

    public String getFullArrayString() {
        return this.getDisplayName() + ChatFormatting.GRAY + (this.getDisplayInfo() != null ? " [" + ChatFormatting.WHITE + this.getDisplayInfo() + ChatFormatting.GRAY + "]" : "");
    }

    @Override
    public JsonElement toJson() {
        JsonObject object = new JsonObject();
        for (Setting<?> setting : getSettings()) {
            try {
                if (setting.getValue() instanceof Bind bind) {
                    object.addProperty(setting.getName(), bind.getKey());
                } else if (setting.getValue() instanceof java.awt.Color color) {
                    object.addProperty(setting.getName(), color.getRed() + "," + color.getGreen() + "," + color.getBlue() + "," + color.getAlpha());
                } else if (setting.getValue() instanceof Vector2f pos) {
                    object.addProperty(setting.getName(), pos.x() + "," + pos.y());
                } else {
                    object.addProperty(setting.getName(), setting.getValueAsString());
                }
            } catch (Throwable e) {
            }
        }
        return object;
    }

    @Override
    public void fromJson(JsonElement element) {
        if (element == null || element.isJsonNull()) return;
        JsonObject object = element.getAsJsonObject();
        if (object.has("Enabled")) {
            String enabled = object.get("Enabled").getAsString();
            if (Boolean.parseBoolean(enabled)) toggle();
        }
        for (Setting<?> setting : getSettings()) {
            try {
                JsonElement settingElement = object.get(setting.getName());
                if (settingElement != null && !settingElement.isJsonNull()) {
                    ConfigManager.setValueFromJson(this, setting, settingElement);
                }
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
    }

    public enum Category {
        COMBAT("Combat"),
        MISC("Misc"),
        RENDER("Render"),
        MOVEMENT("Movement"),
        PLAYER("Player"),
        CLIENT("Client"),
        HUD("Hud");

        private final String name;

        Category(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }
    }
}
