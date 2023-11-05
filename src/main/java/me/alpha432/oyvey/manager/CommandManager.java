package me.alpha432.oyvey.manager;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import me.alpha432.oyvey.features.Feature;
import me.alpha432.oyvey.features.commands.Command;
import me.alpha432.oyvey.features.commands.impl.*;
import me.alpha432.oyvey.util.traits.Jsonable;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class CommandManager
        extends Feature implements Jsonable {
    private final List<Command> commands = new ArrayList<>();
    private String clientMessage = "<OyVey>";
    private String prefix = ".";

    public CommandManager() {
        super("Command");
        commands.add(new ToggleCommand());
        commands.add(new BindCommand());
        commands.add(new FriendCommand());
        commands.add(new ModuleCommand());
        commands.add(new PrefixCommand());

        commands.add(new HelpCommand());
    }

    public static String[] removeElement(String[] input, int indexToDelete) {
        LinkedList<String> result = new LinkedList<>();
        for (int i = 0; i < input.length; ++i) {
            if (i == indexToDelete) continue;
            result.add(input[i]);
        }
        return result.toArray(input);
    }

    private static String strip(String str, String key) {
        if (str.startsWith(key) && str.endsWith(key)) {
            return str.substring(key.length(), str.length() - key.length());
        }
        return str;
    }

    public void executeCommand(String command) {
        String[] parts = command.split(" (?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
        String name = parts[0].substring(1);
        String[] args = CommandManager.removeElement(parts, 0);
        for (int i = 0; i < args.length; ++i) {
            if (args[i] == null) continue;
            args[i] = CommandManager.strip(args[i], "\"");
        }
        for (Command c : this.commands) {
            if (!c.getName().equalsIgnoreCase(name)) continue;
            c.execute(parts);
            return;
        }
        Command.sendMessage(Formatting.GRAY + "Command not found, type 'help' for the commands list.");
    }

    public Command getCommandByName(String name) {
        for (Command command : this.commands) {
            if (!command.getName().equals(name)) continue;
            return command;
        }
        return null;
    }

    public List<Command> getCommands() {
        return this.commands;
    }

    public String getClientMessage() {
        return this.clientMessage;
    }

    public void setClientMessage(String clientMessage) {
        this.clientMessage = clientMessage;
    }

    public String getPrefix() {
        return this.prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    @Override public JsonElement toJson() {
        JsonObject object = new JsonObject();
        object.addProperty("prefix", prefix);
        return object;
    }

    @Override public void fromJson(JsonElement element) {
        setPrefix(element.getAsJsonObject().get("prefix").getAsString());
    }

    @Override public String getFileName() {
        return "commands.json";
    }
}