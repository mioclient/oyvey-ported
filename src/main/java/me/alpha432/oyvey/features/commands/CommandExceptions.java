package me.alpha432.oyvey.features.commands;

import com.mojang.brigadier.LiteralMessage;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;

public class CommandExceptions {
    public static SimpleCommandExceptionType invalidArgument(String message, Object... format) {
        return new SimpleCommandExceptionType(new LiteralMessage(String.format(message, format)));
    }
}
