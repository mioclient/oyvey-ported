package me.alpha432.oyvey.features.commands.impl;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import me.alpha432.oyvey.features.commands.Command;
import me.alpha432.oyvey.manager.CommandManager;

import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

import static me.alpha432.oyvey.features.commands.argument.CommandArgumentType.command;
import static me.alpha432.oyvey.features.commands.argument.CommandArgumentType.getCommand;

public class HelpCommand extends Command {
    public HelpCommand() {
        super(new String[]{"help", "commands", "h", "cmds"},
                "Displays all executable commands and additional information");
    }

    @Override
    public void createArgumentBuilder(LiteralArgumentBuilder<CommandManager> builder) {
        var defaultContext = (com.mojang.brigadier.Command<CommandManager>) (ctx) -> {
            List<Command> commands = ctx.getSource().getCommands()
                    .stream()
                    .filter(Command::isShown)
                    .toList();
            StringJoiner joiner = new StringJoiner(", ");
            for (Command executor : commands) {
                joiner.add(executor.getAliases()[0]);
            }
            return success("{gray} Commands (%s): {reset} %s", commands.size(), joiner);
        };

        builder
                .then(argument("command", command())
                        .executes((ctx) -> {
                            Command command = getCommand(ctx, "command");
                            StringJoiner joiner = new StringJoiner(", ");
                            for (String alias : command.getAliases()) {
                                joiner.add(alias);
                            }

                            sendMessage("{gray} Aliases (%s): {reset} %s",
                                    command.getAliases().length, joiner);
                            return success("{gray} Description: {reset} %s",
                                    Objects.requireNonNullElse(command.getDescription(), DEFAULT_COMMAND_DESCRIPTION));
                        }))
                .executes(defaultContext);
    }
}
