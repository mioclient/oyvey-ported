package me.alpha432.oyvey.features;

import me.alpha432.oyvey.features.settings.Setting;
import me.alpha432.oyvey.util.traits.Util;

import java.util.ArrayList;
import java.util.List;

public class Feature
        implements Util {
    public List<Setting<?>> settings = new ArrayList<>();
    private String name;

    public Feature() {
    }

    public Feature(String name) {
        this.name = name;
    }

    public static boolean nullCheck() {
        return Feature.mc.player == null;
    }

    public static boolean fullNullCheck() {
        return Feature.mc.player == null || Feature.mc.world == null;
    }

    public String getName() {
        return this.name;
    }

    public List<Setting<?>> getSettings() {
        return this.settings;
    }

    public boolean hasSettings() {
        return !this.settings.isEmpty();
    }

    public boolean isEnabled() {
        return false;
    }

    public boolean isDisabled() {
        return !this.isEnabled();
    }

    public <T> Setting<T> register(Setting<T> setting) {
        setting.setFeature(this);
        this.settings.add(setting);
//        if (this instanceof Module && Feature.mc.currentScreen instanceof OyVeyGui) {
//            OyVeyGui.getInstance().updateModule((Module) this);
//        }
        return setting;
    }

    public void unregister(Setting<?> settingIn) {
        ArrayList<Setting<?>> removeList = new ArrayList<>();
        for (Setting<?> setting : this.settings) {
            if (!setting.equals(settingIn)) continue;
            removeList.add(setting);
        }
        if (!removeList.isEmpty()) {
            this.settings.removeAll(removeList);
        }
//        if (this instanceof Module && Feature.mc.currentScreen instanceof OyVeyGui) {
//            OyVeyGui.getInstance().updateModule((Module) this);
//        }
    }

    public Setting<?> getSettingByName(String name) {
        for (Setting<?> setting : this.settings) {
            if (!setting.getName().equalsIgnoreCase(name)) continue;
            return setting;
        }
        return null;
    }

    public void reset() {
        for (Setting<?> setting : this.settings) {
            setting.reset();
        }
    }

    public void clearSettings() {
        this.settings = new ArrayList<>();
    }
}