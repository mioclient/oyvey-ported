package me.alpha432.oyvey.features.settings;

import org.joml.Vector2f;

import java.awt.*;

public interface SettingFactory {
    <T extends Setting<?>> T register(T setting);

    default Setting<Boolean> bool(String name, boolean value) {
        return register(new Setting<>(name, value));
    }

    default <T extends Number> Setting<T> num(String name, T value, T min, T max) {
        return register(new Setting<>(name, value, min, max));
    }

    default Setting<String> str(String name, String value) {
        return register(new Setting<>(name, value));
    }

    default <T extends Enum<?>> Setting<T> mode(String name, T value) {
        return register(new Setting<>(name, value));
    }

    default Setting<Bind> key(String name, Bind bind) {
        return register(new Setting<>(name, bind));
    }

    default Setting<Color> color(String name, Color value) {
        return register(new Setting<>(name, value));
    }

    default Setting<Color> color(String name, int r, int g, int b, int a) {
        return register(new Setting<>(name, new Color(r, g, b, a)));
    }

    default Setting<Vector2f> vec2f(String name, Vector2f value) {
        return register(new Setting<>(name, value));
    }

    default Setting<Vector2f> vec2f(String name, float x, float y) {
        return register(new Setting<>(name, new Vector2f(x, y)));
    }

}
