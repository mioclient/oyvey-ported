package me.alpha432.oyvey.features.settings;

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

}
