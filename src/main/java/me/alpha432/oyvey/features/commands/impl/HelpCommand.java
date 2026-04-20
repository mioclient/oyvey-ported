package me.alpha432.oyvey.features.commands.impl;

import com.google.common.collect.Iterables;
import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.CommandNode;
import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.features.commands.Command;
import me.alpha432.oyvey.manager.CommandManager;
import me.alpha432.oyvey.util.chat.ChatUtil;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.chat.MutableComponent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static me.alpha432.oyvey.features.commands.MessageSignatures.GENERAL;
import static me.alpha432.oyvey.features.commands.argument.CommandArgumentType.command;
import static me.alpha432.oyvey.features.commands.argument.CommandArgumentType.getCommand;
import static me.alpha432.oyvey.features.commands.argument.NumberArgumentType.get;
import static me.alpha432.oyvey.features.commands.argument.NumberArgumentType.number;
import static me.alpha432.oyvey.util.TextUtil.text;
import static me.alpha432.oyvey.util.chat.ChatUtil.prefix;
import static me.alpha432.oyvey.util.chat.ChatUtil.withPrefix;

public class HelpCommand extends Command {
    private static final int ITEMS_PER_PAGE = 5;

    public HelpCommand() {
        super("help", "commands", "h", "cmds");
        setDescription("Displays all executable commands and additional information");
    }

    @Override
    public void createArgumentBuilder(LiteralArgumentBuilder<CommandManager> builder) {
        builder.then(argument("page", number(Integer.class))
                        .executes((ctx) -> helpCommands(ctx, get(Integer.class, ctx, "page"))))
                .then(argument("command_name", command())
                        .executes(this::helpSpecific))
                .executes((ctx) -> helpCommands(ctx, 1));
    }

    private int helpSpecific(CommandContext<CommandManager> ctx) {
        Command command = getCommand(ctx, "command_name");

        MutableComponent text = Component.empty();
        text.append(withPrefix("Usages for %s:\n", command.getName()));

        ParseResults<CommandManager> results = ctx.getSource().getDispatcher()
                .parse(command.getName(), ctx.getSource());

        if (results.getContext().getNodes().isEmpty()) {
            text.append(withPrefix("No usages available"));
        } else {
            Map<CommandNode<CommandManager>, String> smartUsages = ctx.getSource().getDispatcher().getSmartUsage(
                    Iterables.getLast(results.getContext().getNodes()).getNode(), ctx.getSource());
            List<Component> usages = new ArrayList<>();

            for (String usage : smartUsages.values()) {
                usages.add(text("   {gray}.%s %s", command.getName(), usage));
            }

            text.append(ComponentUtils.formatList(usages, Component.literal("\n")));
        }

        ChatUtil.sendClientSideMessage(text, GENERAL);
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

        MutableComponent text = Component.empty();
        text.append(withPrefix("Commands (%s): \n", commands.size()));

        for (Command command : paginated) {
            String aliases = String.join(", ", command.getAliases());
            String description = command.getDescription();
            text.append(withPrefix("{dark_gray} %s: {reset}\n   %s\n", aliases, description));
        }

        text.append(buildPages(pageIndex, pages));

        ChatUtil.sendClientSideMessage(text, GENERAL);
        return success();
    }

    private static Component buildPages(int index, int pages) {
        String command = OyVey.commandManager.getCommandPrefix() + "help ";
        MutableComponent text = prefix();
        boolean hasPages = pages > 1;

        if (hasPages && index != 1)
            text.append(suggest(Component.literal(" <"), command + (index - 1)));

        text.append(text(" Page %d/%d", index, pages));

        if (hasPages && index != pages)
            text.append(suggest(Component.literal(" > "), command + (index + 1)));

        return text;
    }

    private static Component suggest(MutableComponent component, String command) {
        return component.withStyle(s -> s.withClickEvent(new ClickEvent.SuggestCommand(command.formatted())));
    }
}
