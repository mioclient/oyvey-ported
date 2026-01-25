package me.alpha432.oyvey.features.commands.argument;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import me.alpha432.oyvey.features.commands.CommandExceptions;

import java.awt.*;
import java.util.Collection;
import java.util.List;

public class ColorArgumentType implements ArgumentType<Color> {
    private static final List<String> EXAMPLES = List.of("hsb:55,100,100", "255,0,255", "0,255,255,80");
    private static final String[] RGBA_VALUES = {"Red", "Green", "Blue", "Alpha"};

    @Override
    public Color parse(StringReader reader) throws CommandSyntaxException {
        String value = reader.getRemaining();
        reader.setCursor(reader.getTotalLength());

        // if input begins with "#" its a hex color
        if (value.startsWith("#")) {
            return new Color(Integer.parseInt(value.substring(1), 16));
        }

        if (value.startsWith("hsb:")) {
            int[] elements = parseColorElements(reader, value.substring(4));

            int hue = elements[0];
            if (hue < 0 || hue > 360) {
                throw CommandExceptions.invalidArgument("Hue value must be between 0-360")
                        .createWithContext(reader);
            }

            int saturation = elements[1];
            if (saturation < 0 || saturation > 100) {
                throw CommandExceptions.invalidArgument("Saturation value must be between 0-100")
                        .createWithContext(reader);
            }

            int brightness = elements[2];
            if (brightness < 0 || brightness > 100) {
                throw CommandExceptions.invalidArgument("Brightness value must be between 0-100")
                        .createWithContext(reader);
            }

            return Color.getHSBColor((float) hue / 360.0f,
                    (float) saturation / 100.0f,
                    (float) brightness / 100.0f);
        }

        int[] elements = parseColorElements(reader, value);
        for (int i = 0; i < elements.length; ++i) {
            int element = elements[i];
            if (element < 0 || element > 255) {
                throw CommandExceptions.invalidArgument("%s value must be between 0-255", RGBA_VALUES[i])
                        .createWithContext(reader);
            }
        }

        return new Color(elements[0],
                elements[1],
                elements[2],
                elements.length == 4 ? elements[3] : 255);
    }

    private int[] parseColorElements(StringReader reader, String value) throws CommandSyntaxException {
        String[] parts = value.split(",");
        if (parts.length < 3) {
            throw CommandExceptions.invalidArgument("Color element must have three values, either RGB or hsb:HSB")
                    .createWithContext(reader);
        }

        int[] elements = new int[parts.length];
        for (int i = 0; i < parts.length; ++i) {
            elements[i] = Integer.parseInt(parts[i]);
        }
        return elements;
    }

    @Override
    public Collection<String> getExamples() {
        return EXAMPLES;
    }

    public static Color getColor(CommandContext<?> ctx, String name) {
        return ctx.getArgument(name, Color.class);
    }

    public static ColorArgumentType color() {
        return new ColorArgumentType();
    }
}
