package me.alpha432.oyvey.manager;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.event.impl.render.Render2DEvent;
import me.alpha432.oyvey.event.impl.render.Render3DEvent;
import me.alpha432.oyvey.features.Feature;
import me.alpha432.oyvey.features.commands.ModuleCommand;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.modules.client.ClickGuiModule;
import me.alpha432.oyvey.features.modules.client.HudEditorModule;
import me.alpha432.oyvey.features.modules.client.NotificationsModule;
import me.alpha432.oyvey.features.modules.combat.CriticalsModule;
import me.alpha432.oyvey.features.modules.combat.KeyPearlModule;
import me.alpha432.oyvey.features.modules.hud.CoordinatesHudModule;
import me.alpha432.oyvey.features.modules.hud.WatermarkHudModule;
import me.alpha432.oyvey.features.modules.misc.MCFModule;
import me.alpha432.oyvey.features.modules.movement.ReverseStepModule;
import me.alpha432.oyvey.features.modules.movement.StepModule;
import me.alpha432.oyvey.features.modules.player.FastPlaceModule;
import me.alpha432.oyvey.features.modules.player.NoFallModule;
import me.alpha432.oyvey.features.modules.player.VelocityModule;
import me.alpha432.oyvey.features.modules.render.BlockHighlightModule;
import me.alpha432.oyvey.util.traits.Jsonable;
import me.alpha432.oyvey.util.traits.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Stream;

public class ModuleManager implements Jsonable, Util {
    private static final Logger LOGGER = LoggerFactory.getLogger("ModuleManager");

    private final Map<Class<? extends Module>, Module> fastRegistry = new HashMap<>();
    private final List<Module> modules = new ArrayList<>();

    public void init() {
        register(new WatermarkHudModule());
        register(new CoordinatesHudModule());
        register(new HudEditorModule());
        register(new ClickGuiModule());
        register(new NotificationsModule());
        register(new CriticalsModule());
        register(new MCFModule());
        register(new StepModule());
        register(new ReverseStepModule());
        register(new FastPlaceModule());
        register(new VelocityModule());
        register(new BlockHighlightModule());
        register(new NoFallModule());
        register(new KeyPearlModule());

        LOGGER.info("Registered {} modules", modules.size());

        // Create a command for each module for modules to be configurable via command line
        for (Module module : modules) {
            OyVey.commandManager.registerExecutor(new ModuleCommand(module));
        }

        OyVey.configManager.addConfig(this);
    }

    public void register(Module module) {
        getModules().add(module);
        fastRegistry.put(module.getClass(), module);
    }

    public List<Module> getModules() {
        return modules;
    }

    public Stream<Module> stream() {
        return getModules().stream();
    }

    @SuppressWarnings("unchecked")
    public <T extends Module> T getModuleByClass(Class<T> clazz) {
        return (T) fastRegistry.get(clazz);
    }

    public Module getModuleByName(String name) {
        return stream().filter(m -> m.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public Module getModuleByDisplayName(String display) {
        return stream().filter(m -> m.getDisplayName().equalsIgnoreCase(display)).findFirst().orElse(null);
    }

    public List<Module> getModulesByCategory(Module.Category category) {
        return stream().filter(m -> m.getCategory() == category).toList();
    }

    public List<Module.Category> getCategories() {
        return Arrays.asList(Module.Category.values());
    }

    public void onLoad() {
        getModules().forEach(Module::onLoad);
    }

    public void onTick() {
        stream().filter(Feature::isEnabled).forEach(Module::onTick);
    }

    public void onRender2D(Render2DEvent event) {
        stream().filter(Feature::isEnabled).forEach(module -> module.onRender2D(event));
    }

    public void onRender3D(Render3DEvent event) {
        stream().filter(Feature::isEnabled).forEach(module -> module.onRender3D(event));
    }

    public void onUnload() {
        getModules().forEach(EVENT_BUS::unregister);
        getModules().forEach(Module::onUnload);
    }

    public void onKeyPressed(int key) {
        if (key <= 0 || mc.screen != null) return;
        stream().filter(module -> module.getBind().getKey() == key).forEach(Module::toggle);
    }

    @Override
    public JsonElement toJson() {
        JsonObject object = new JsonObject();
        for (Module module : getModules()) {
            object.add(module.getName(), module.toJson());
        }
        return object;
    }

    @Override
    public void fromJson(JsonElement element) {
        for (Module module : getModules()) {
            module.fromJson(element.getAsJsonObject().get(module.getName()));
        }
    }

    @Override
    public String getFileName() {
        return "modules.json";
    }
}
