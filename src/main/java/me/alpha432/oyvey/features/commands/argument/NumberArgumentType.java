package me.alpha432.oyvey.features.commands.argument;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import me.alpha432.oyvey.features.commands.CommandExceptions;

public record NumberArgumentType<T extends Number>(Class<T> type, Constraint<T> constraint) implements ArgumentType<T> {
    @SuppressWarnings("unchecked")
    @Override
    public T parse(StringReader reader) throws CommandSyntaxException {
        String input = reader.readString();

        T value = null;
        if (type.isAssignableFrom(Integer.class)) {
            value = (T) Integer.valueOf(input);
        } else if (type.isAssignableFrom(Double.class)) {
            value = (T) Double.valueOf(input);
        } else if (type.isAssignableFrom(Float.class)) {
            value = (T) Float.valueOf(input);
        } else if (type.isAssignableFrom(Long.class)) {
            value = (T) Long.valueOf(input);
        }

        if (constraint != null) {
            constraint.validate(reader, value);
        }

        if (value == null) {
            throw CommandExceptions.invalidArgument("Could not parse \"%s\"", input)
                    .createWithContext(reader);
        }

        return value;
    }

    public static <T extends Number> Constraint<T> minMax(T min, T max) {
        return (reader, input) -> {
            double value = input.doubleValue();
            if (min.doubleValue() > value) {
                throw CommandExceptions.invalidArgument("Value is less than minimum(%s)", min.toString())
                        .createWithContext(reader);
            }
            if (max.doubleValue() < value) {
                throw CommandExceptions.invalidArgument("Value is more than maximum(%s)", max.toString())
                        .createWithContext(reader);
            }
        };
    }

    public static <T extends Number> T get(Class<T> type, CommandContext<?> ctx, String name) {
        return ctx.getArgument(name, type);
    }

    public static <T extends Number> NumberArgumentType<T> number(Class<T> type) {
        return number(type, null);
    }

    public static <T extends Number> NumberArgumentType<T> number(Class<T> type, Constraint<T> constraint) {
        return new NumberArgumentType<>(type, constraint);
    }

    public interface Constraint<T extends Number> {
        void validate(StringReader reader, T input) throws CommandSyntaxException;
    }
}
