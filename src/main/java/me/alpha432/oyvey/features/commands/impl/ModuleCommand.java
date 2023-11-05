package me.alpha432.oyvey.features.commands.impl;

import com.google.gson.JsonParser;
import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.features.commands.Command;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.settings.Setting;
import me.alpha432.oyvey.manager.ConfigManager;
import net.minecraft.util.Formatting;

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
                String modules = category.getName() + ": ";
                for (Module module1 : OyVey.moduleManager.getModulesByCategory(category)) {
                    modules = modules + (module1.isEnabled() ? Formatting.GREEN : Formatting.RED) + module1.getName() + Formatting.WHITE + ", ";
                }
                ModuleCommand.sendMessage(modules);
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
            ModuleCommand.sendMessage(" This is the original name of the module. Its current name is: " + module.getDisplayName());
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
            JsonParser jp = new JsonParser();
            if (setting.getType().equalsIgnoreCase("String")) {
                setting.setValue(commands[3]);
                ModuleCommand.sendMessage(Formatting.DARK_GRAY + module.getName() + " " + setting.getName() + " has been set to " + commands[3] + ".");
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
                ConfigManager.setValueFromJson(module, setting, jp.parse(commands[3]));
            } catch (Exception e) {
                ModuleCommand.sendMessage("Bad Value! This setting requires a: " + setting.getType() + " value.");
                return;
            }
            ModuleCommand.sendMessage(Formatting.GRAY + module.getName() + " " + setting.getName() + " has been set to " + commands[3] + ".");
        }
    }
}