package me.alpha432.oyvey.features.commands.impl;

import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.features.commands.Command;
import me.alpha432.oyvey.features.modules.Module;

public class ToggleCommand extends Command {
    public ToggleCommand() {
        super("toggle", new String[] {"<module>"});
    }

    @Override public void execute(String[] var1) {
        if (var1.length < 1 || var1[0] == null) {
            notFound();
            return;
        }
        Module mod = OyVey.moduleManager.getModuleByName(var1[0]);
        if (mod == null) {
            notFound();
            return;
        }
        mod.toggle();
    }

    private void notFound() {
        sendMessage("Module is not found.");
    }
}
