package me.alpha432.oyvey.features.commands.argument;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandExceptionType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.features.commands.Command;
import me.alpha432.oyvey.features.commands.CommandExceptions;

import java.util.concurrent.CompletableFuture;

public class CommandArgumentType implements ArgumentType<Command>, CommandExceptionType {
    @Override
    public Command parse(StringReader reader) throws CommandSyntaxException {
        String value = reader.readString().toLowerCase();

        for (String alias : OyVey.commandManager.getCommandAliases()) {
            if (value.equalsIgnoreCase(alias)) {
                return OyVey.commandManager.getCommand(alias);
            }
        }

        throw CommandExceptions.invalidArgument("Invalid command").createWithContext(reader);
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        String input = builder.getInput().substring(builder.getStart()).toLowerCase();

        for (String alias : OyVey.commandManager.getCommandAliases()) {
            String name = alias.toLowerCase();
            if (name.contains(input)) {
                builder.suggest(name);
            }
        }

        return builder.buildFuture();
    }

    public static Command getCommand(CommandContext<?> ctx, String name) {
        return ctx.getArgument(name, Command.class);
    }

    public static CommandArgumentType command() {
        return new CommandArgumentType();
    }
}
