package me.alpha432.oyvey.util;

import dev.cattyn.catformat.CatFormat;
import dev.cattyn.catformat.fabric.FabricCatFormat;
import me.alpha432.oyvey.OyVey;
import net.minecraft.text.MutableText;

public final class TextUtil {
    private static CatFormat<MutableText> formatter;

    private TextUtil() {
        throw new AssertionError("Can't create an instance of utility class");
    }

    public static void init() {
        if (formatter != null) throw new IllegalStateException("Formatter is already initialized");
        formatter = new FabricCatFormat(true);
        initColors();
    }

    public static MutableText text(String content, Object... obj) {
        if (obj == null || obj.length == 0) {
            return formatter.format(content);
        }
        return formatter.format(content, obj);
    }

    private static void initColors() {
        formatter.add("global", () -> OyVey.colorManager.getColorAsInt());
    }
}
