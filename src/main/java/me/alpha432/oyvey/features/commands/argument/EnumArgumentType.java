package me.alpha432.oyvey.features.commands.argument;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import me.alpha432.oyvey.features.commands.CommandExceptions;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class EnumArgumentType<T extends Enum<?>> implements ArgumentType<T> {
    private final Map<String, T> enumNameMap = new HashMap<>();
    private final T defaultValue;

    public EnumArgumentType(Class<T> clazz, T defaultValue) {
        this.defaultValue = defaultValue;

        for (T constant : clazz.getEnumConstants()) {
            enumNameMap.put(constant.toString(), constant);
        }
    }

    @Override
    public T parse(StringReader reader) throws CommandSyntaxException {
        String value = reader.readString();
        for (String name : enumNameMap.keySet()) {
            if (value.equalsIgnoreCase(name)) {
                return enumNameMap.get(name);
            }
        }

        if (defaultValue != null) {
            return defaultValue;
        }

        throw CommandExceptions.invalidArgument("Invalid enum type").createWithContext(reader);
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        String input = builder.getRemainingLowerCase();
        for (String key : enumNameMap.keySet()) {
            String name = key.toLowerCase();
            if (name.contains(input)) {
                builder.suggest(name);
            }
        }
        return builder.buildFuture();
    }

    @SuppressWarnings("unchecked")
    public static <T extends Enum<?>> T getEnum(CommandContext<?> ctx, String name) {
        return (T) ctx.getArgument(name, Object.class);
    }

    public static <T extends Enum<?>> EnumArgumentType<T> _enum(Class<T> clazz) {
        return _enum(clazz, null);
    }

    public static <T extends Enum<?>> EnumArgumentType<T> _enum(Class<T> clazz, T defaultValue) {
        return new EnumArgumentType<>(clazz, defaultValue);
    }
}
