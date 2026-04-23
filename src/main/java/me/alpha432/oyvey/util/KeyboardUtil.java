package me.alpha432.oyvey.util;

import com.mojang.blaze3d.platform.InputConstants;
import me.alpha432.oyvey.features.settings.Bind;
import net.minecraft.client.input.KeyEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class KeyboardUtil {
    public static String getKeyName(int key, boolean mouse) {
        return getKeyName(mouse ? Bind.mouse(key) : Bind.keyboard(key));
    }

    public static String getKeyName(Bind bind) {
        if (bind.isEmpty()) return "NONE";
        if (bind.isMouse()) return "MOUSE" + (-bind.getKey() - 1);
        return getKeyName(InputConstants.getKey(new KeyEvent(bind.getKey(), 0, 0)));
    }

    public static String getKeyName(InputConstants.Key key) {
        return key.getName()
                .replace("key.mouse.", "")
                .replace("key.keyboard.", "")
                .replace(".", "_")
                .toUpperCase(Locale.ROOT);
    }

    public static Bind getBind(String name) {
        String lowercaseName = name.toLowerCase(Locale.ROOT).replace("_", ".");
        if (name.startsWith("mouse")) {
            try {
                return new Bind(-Integer.parseInt(name.substring(5)) - 1);
            } catch (Exception e) {
                return null;
            }
        }

        try {
            InputConstants.Key key = InputConstants.getKey("key.keyboard." + lowercaseName);
            return new Bind(key.getValue());
        } catch (IllegalArgumentException exception) {
            return null;
        }
    }

    public static Iterable<String> iterateKeys() {
        List<String> keys = new ArrayList<>();
        for (InputConstants.Key key : InputConstants.Type.KEYSYM.map.values())
            keys.add(getKeyName(key));
        for (int i = 1; i <= 9; i++) {
            keys.add("MOUSE" + i);
        }
        return keys;
    }
}
