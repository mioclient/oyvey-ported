package me.alpha432.oyvey.features.commands;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import me.alpha432.oyvey.features.Feature;
import me.alpha432.oyvey.manager.CommandManager;
import me.alpha432.oyvey.util.TextUtil;
import me.alpha432.oyvey.util.player.ChatUtil;

import static com.mojang.brigadier.Command.SINGLE_SUCCESS;

public abstract class Command extends Feature {
    protected static final String DEFAULT_COMMAND_DESCRIPTION
            = "No description was provided for this command.";

    public static final int NO_OP = -1;
    public static final int SINGLE_FAILURE = 0;

    private final String[] aliases;
    private final String description;

    public Command(String[] aliases, String description) {
        this.aliases = aliases;
        this.description = description;
    }

    public Command(String[] aliases) {
        this(aliases, DEFAULT_COMMAND_DESCRIPTION);
    }

    public abstract void createArgumentBuilder(LiteralArgumentBuilder<CommandManager> builder);

    protected int success(String message, Object... format) {
        sendMessage(message, format);
        return NO_OP;
    }

    protected int success() {
        return SINGLE_SUCCESS;
    }

    protected int fail(String message, Object... format) {
        sendMessage("{red} " + message, format);
        return NO_OP;
    }

    protected int fail() {
        return SINGLE_FAILURE;
    }

    public String[] getAliases() {
        return aliases;
    }

    public String getDescription() {
        return description;
    }

    public static void sendMessage(String message, Object... format) {
        if (message == null) {
            return;
        }
        ChatUtil.sendMessage(TextUtil.text(message, format));
    }

    public static LiteralArgumentBuilder<CommandManager> literal(String literal) {
        return LiteralArgumentBuilder.literal(literal);
    }

    public static <T> RequiredArgumentBuilder<CommandManager, T> argument(String name, ArgumentType<T> type) {
        return RequiredArgumentBuilder.argument(name, type);
    }
}
