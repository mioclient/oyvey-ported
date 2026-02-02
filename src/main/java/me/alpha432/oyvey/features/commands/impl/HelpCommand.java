package me.alpha432.oyvey.features.commands.impl;

import com.google.common.collect.Iterables;
import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.CommandNode;
import me.alpha432.oyvey.features.commands.Command;
import me.alpha432.oyvey.manager.CommandManager;

import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

import static me.alpha432.oyvey.features.commands.argument.CommandArgumentType.command;
import static me.alpha432.oyvey.features.commands.argument.CommandArgumentType.getCommand;
import static me.alpha432.oyvey.features.commands.argument.NumberArgumentType.get;
import static me.alpha432.oyvey.features.commands.argument.NumberArgumentType.number;

public class HelpCommand extends Command {
    private static final int ITEMS_PER_PAGE = 5;

    public HelpCommand() {
        super("help", "commands", "h", "cmds");
        setDescription("Displays all executable commands and additional information");
    }

    @Override
    public void createArgumentBuilder(LiteralArgumentBuilder<CommandManager> builder) {
        builder.then(argument("page", number(Integer.class))
                        .executes((ctx) -> helpCommands(ctx, get(Integer.class, ctx, "page")))
                .then(argument("command_name", command())
                        .executes(this::helpSpecific))
                .executes((ctx) -> helpCommands(ctx, 1)));
    }

    private int helpSpecific(CommandContext<CommandManager> ctx) {
        Command command = getCommand(ctx, "command_name");
        sendMessage("Usages for %s:", command.getName());

        ParseResults<CommandManager> results = ctx.getSource().getDispatcher()
                .parse(command.getName(), ctx.getSource());
        if (results.getContext().getNodes().isEmpty()) {
            return success("No usages available");
        }

        Map<CommandNode<CommandManager>, String> smartUsages = ctx.getSource().getDispatcher().getSmartUsage(
                Iterables.getLast(results.getContext().getNodes()).getNode(), ctx.getSource());
        for (String usage : smartUsages.values()) {
            sendMessage(".%s %s", results.getReader().getString(), usage);
        }

        return success();
    }

    private int helpCommands(CommandContext<CommandManager> ctx, int page) {
        List<Command> commands = ctx.getSource().getCommands()
                .stream().filter(Command::isShown).toList();

        int pageIndex = page;
        int pages = (int) Math.ceil((double) commands.size() / ITEMS_PER_PAGE);
        if (pageIndex > pages) {
            pageIndex = 1;
        }

        List<Command> paginated = commands.subList((pageIndex - 1) * ITEMS_PER_PAGE,
                Math.min(commands.size(), pageIndex * ITEMS_PER_PAGE));

        sendMessage("Commands (%s):", commands.size());

        for (Command command : paginated) {
            StringJoiner joiner = new StringJoiner(", ");
            for (String alias : command.getAliases()) {
                joiner.add(alias);
            }

            sendMessage("{dark_gray} %s: {reset}\n   %s",
                    joiner, command.getDescription());
        }

        return success("Page %s/%s", pageIndex, pages);
    }
}
