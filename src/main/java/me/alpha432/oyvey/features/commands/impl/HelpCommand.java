package me.alpha432.oyvey.features.commands.impl;

import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.features.commands.Command;
import net.minecraft.ChatFormatting;

public class HelpCommand
        extends Command {
    public HelpCommand() {
        super("help");
    }

    @Override
    public void execute(String[] commands) {
        HelpCommand.sendMessage("Commands: ");
        for (Command command : OyVey.commandManager.getCommands()) {
            StringBuilder builder = new StringBuilder(ChatFormatting.GRAY.toString());
            builder.append(OyVey.commandManager.getPrefix());
            builder.append(command.getName());
            builder.append(" ");
            for (String cmd : command.getCommands()) {
                builder.append(cmd);
                builder.append(" ");
            }
            HelpCommand.sendMessage(builder.toString());
        }
    }
}