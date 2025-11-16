package me.alpha432.oyvey.features.commands.impl;

import com.google.gson.JsonParser;
import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.features.commands.Command;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.settings.Setting;
import me.alpha432.oyvey.manager.ConfigManager;
import net.minecraft.ChatFormatting;

public class ModuleCommand
        extends Command {
    public ModuleCommand() {
        super("module", new String[]{"<module>", "<set/reset>", "<setting>", "<value>"});
    }

    @Override
    public void execute(String[] commands) {
        Setting setting;
        if (commands.length == 1) {
            ModuleCommand.sendMessage("Modules: ");
            for (Module.Category category : OyVey.moduleManager.getCategories()) {
                StringBuilder modules = new StringBuilder(category.getName() + ": ");
                for (Module module : OyVey.moduleManager.getModulesByCategory(category)) {
                    modules.append(module.isEnabled() ? ChatFormatting.GREEN : ChatFormatting.RED).append(module.getName()).append(ChatFormatting.WHITE).append(", ");
                }
                ModuleCommand.sendMessage(modules.toString());
            }
            return;
        }
        Module module = OyVey.moduleManager.getModuleByDisplayName(commands[0]);
        if (module == null) {
            module = OyVey.moduleManager.getModuleByName(commands[0]);
            if (module == null) {
                ModuleCommand.sendMessage("This module doesnt exist.");
                return;
            }
            ModuleCommand.sendMessage(" This is the original name of the module. Its current name is: %s", module.getDisplayName());
            return;
        }
        if (commands.length == 2) {
            ModuleCommand.sendMessage(module.getDisplayName() + " : " + module.getDescription());
            for (Setting setting2 : module.getSettings()) {
                ModuleCommand.sendMessage(setting2.getName() + " : " + setting2.getValue() + ", " + setting2.getDescription());
            }
            return;
        }
        if (commands.length == 3) {
            if (commands[1].equalsIgnoreCase("set")) {
                ModuleCommand.sendMessage("Please specify a setting.");
            } else if (commands[1].equalsIgnoreCase("reset")) {
                for (Setting setting3 : module.getSettings()) {
                    setting3.setValue(setting3.getDefaultValue());
                }
            } else {
                ModuleCommand.sendMessage("This command doesnt exist.");
            }
            return;
        }
        if (commands.length == 4) {
            ModuleCommand.sendMessage("Please specify a value.");
            return;
        }
        if (commands.length == 5 && (setting = module.getSettingByName(commands[2])) != null) {
            if (setting.getType().equalsIgnoreCase("String")) {
                setting.setValue(commands[3]);
                ModuleCommand.sendMessage("{dark_gray} %s %s has been set to %s.", module.getName(), setting.getName(), commands[3]);
                return;
            }
            try {
                if (setting.getName().equalsIgnoreCase("Enabled")) {
                    if (commands[3].equalsIgnoreCase("true")) {
                        module.enable();
                    }
                    if (commands[3].equalsIgnoreCase("false")) {
                        module.disable();
                    }
                }
                ConfigManager.setValueFromJson(module, setting, JsonParser.parseString(commands[3]));
            } catch (Exception e) {
                ModuleCommand.sendMessage("Bad Value! This setting requires a: %s value.", setting.getType());
                return;
            }
            ModuleCommand.sendMessage("{gray} %s %s has been set tot %s.", module.getName(), setting.getName(), commands[3]);
        }
    }
}