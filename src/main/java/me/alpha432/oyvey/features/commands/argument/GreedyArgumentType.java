package me.alpha432.oyvey.features.commands.argument;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import java.lang.reflect.Array;
import java.util.concurrent.CompletableFuture;

public record GreedyArgumentType<T>(ArgumentType<T> parent, Class<T> type) implements ArgumentType<T[]> {
    @Override
    @SuppressWarnings("unchecked")
    public T[] parse(StringReader reader) throws CommandSyntaxException {
        String input = reader.getRemaining();
        reader.setCursor(reader.getTotalLength());
        String[] split = input.split(" ");
        T[] values = (T[]) Array.newInstance(type, split.length);
        for (int i = 0; i < split.length; i++) {
            values[i] = parent.parse(new StringReader(split[i]));
        }
        return values;
    }

    @Override public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        String[] s = builder.getRemaining().split(" ", -1);
        if (s.length == 0) {
            return parent.listSuggestions(context, builder);
        }

        int st = builder.getStart() + builder.getRemaining().length() - s[s.length - 1].length();
        return parent.listSuggestions(context, new SuggestionsBuilder(builder.getInput(), st));
    }

    public static <T> GreedyArgumentType<T> greedy(ArgumentType<T> parent, Class<T> type) {
        return new GreedyArgumentType<>(parent, type);
    }
}
