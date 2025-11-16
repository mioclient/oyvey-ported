package me.alpha432.oyvey.features;

import me.alpha432.oyvey.features.settings.Setting;
import me.alpha432.oyvey.features.settings.SettingFactory;
import me.alpha432.oyvey.util.traits.Util;

import java.util.ArrayList;
import java.util.List;

public class Feature
        implements Util, SettingFactory {
    public List<Setting<?>> settings = new ArrayList<>();
    private String name;

    public Feature() {
    }

    public Feature(String name) {
        this.name = name;
    }

    public static boolean nullCheck() {
        return Feature.mc.player == null || Feature.mc.level == null;
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

    public <T extends Setting<?>> T register(T setting) {
        setting.setFeature(this);
        this.settings.add(setting);
        return setting;
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
}