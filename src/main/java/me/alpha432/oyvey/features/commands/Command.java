package me.alpha432.oyvey.features.commands;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import me.alpha432.oyvey.features.Feature;
import me.alpha432.oyvey.manager.CommandManager;
import me.alpha432.oyvey.util.TextUtil;
import me.alpha432.oyvey.util.chat.ChatUtil;
import me.alpha432.oyvey.util.chat.Signature;

import static com.mojang.brigadier.Command.SINGLE_SUCCESS;
import static me.alpha432.oyvey.features.commands.MessageSignatures.FAIL;
import static me.alpha432.oyvey.features.commands.MessageSignatures.SUCCESS;

public abstract class Command extends Feature {
    public static final int NO_OP = -1;
    public static final int SINGLE_FAILURE = 0;

    private final String[] aliases;
    private String description = "No description was provided for this command.";

    public Command(String... aliases) {
        super(aliases[0]);
        this.aliases = aliases;
    }

    public abstract void createArgumentBuilder(LiteralArgumentBuilder<CommandManager> builder);

    protected int success(String message, Object... format) {
        sendMessage(message, SUCCESS, format);
        return NO_OP;
    }

    protected int success() {
        return SINGLE_SUCCESS;
    }

    protected int fail(String message, Object... format) {
        sendMessage("{red} " + message, FAIL, format);
        return NO_OP;
    }

    protected int fail() {
        return SINGLE_FAILURE;
    }

    public String[] getAliases() {
        return aliases;
    }

    protected void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public boolean isShown() {
        return true;
    }

    public static void sendMessage(String message, Signature identifier) {
        if (message == null) return;
        ChatUtil.sendMessage(TextUtil.text(message), identifier);
    }

    public static void sendMessage(String message, Signature identifier, Object... format) {
        if (message == null) return;
        ChatUtil.sendMessage(TextUtil.text(message, format), identifier);
    }

    public static LiteralArgumentBuilder<CommandManager> literal(String literal) {
        return LiteralArgumentBuilder.literal(literal);
    }

    public static <T> RequiredArgumentBuilder<CommandManager, T> argument(String name, ArgumentType<T> type) {
        return RequiredArgumentBuilder.argument(name, type);
    }
}