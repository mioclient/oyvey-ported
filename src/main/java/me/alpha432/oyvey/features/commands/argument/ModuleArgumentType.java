package me.alpha432.oyvey.features.commands.argument;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.features.commands.CommandExceptions;
import me.alpha432.oyvey.features.modules.Module;

import java.util.concurrent.CompletableFuture;

public record ModuleArgumentType(boolean fullName) implements ArgumentType<Module> {
    @Override
    public Module parse(StringReader reader) throws CommandSyntaxException {
        String value = reader.readString().toLowerCase();

        for (Module module : OyVey.moduleManager.getModules()) {
            if (value.equalsIgnoreCase(module.getName()) || module.getName().startsWith(value)) {
                return module;
            }
        }

        throw CommandExceptions.invalidArgument("Invalid module").createWithContext(reader);
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        String input = builder.getRemainingLowerCase();

        for (Module module : OyVey.moduleManager.getModules()) {
            String name = module.getName().toLowerCase();
            String displayName = module.getDisplayName().toLowerCase();
            if (name.contains(input) || displayName.contains(input)) {
                builder.suggest(name);
            }
        }

        return builder.buildFuture();
    }

    public static ModuleArgumentType module() {
        return module(false);
    }

    public static ModuleArgumentType module(boolean fullName) {
        return new ModuleArgumentType(fullName);
    }

    public static Module getModule(CommandContext<?> ctx, String name) {
        return ctx.getArgument(name, Module.class);
    }
}
