package me.alpha432.oyvey.features.commands;

import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import java.util.Locale;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Stream;

public final class ArgumentSuggestions {
    public static CompletableFuture<Suggestions> suggest(Stream<String> stream, SuggestionsBuilder builder) {
        return suggest(stream.toList(), x -> x, builder);
    }

    public static CompletableFuture<Suggestions> suggest(Iterable<String> iterable, SuggestionsBuilder builder) {
        return suggest(iterable, x -> x, builder);
    }

    public static <T> CompletableFuture<Suggestions> suggest(Iterable<T> iterable, Function<T, String> map, SuggestionsBuilder builder) {
        String remaining = builder.getRemainingLowerCase();
        for (T t : iterable) {
            String target = map.apply(t).toLowerCase(Locale.ROOT);
            if (target.equals(remaining)) {
                builder.suggest(target);
                return builder.buildFuture();
            }
        }

        for (T t : iterable) {
            String target = map.apply(t).toLowerCase(Locale.ROOT);
            if (target.contains(remaining)) builder.suggest(target);
        }

        return builder.buildFuture();
    }
}
