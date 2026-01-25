package me.alpha432.oyvey.features.commands.impl;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import me.alpha432.oyvey.features.commands.Command;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.manager.CommandManager;

import static me.alpha432.oyvey.features.commands.argument.ModuleArgumentType.getModule;
import static me.alpha432.oyvey.features.commands.argument.ModuleArgumentType.module;

public class ToggleCommand extends Command {
    public ToggleCommand() {
        super("toggle", "t");
        setDescription("Toggles a module");
    }

    @Override
    public void createArgumentBuilder(LiteralArgumentBuilder<CommandManager> builder) {
        builder.then(argument("module", module(true))
                .executes((ctx) -> {
                    Module module = getModule(ctx, "module");
                    module.toggle();
                    boolean toggled = module.isEnabled();
                    return success("{gray} %s {reset} is now %s %s",
                            module.getDisplayName(),
                            toggled ? "{green}" : "{red}",
                            toggled ? "enabled" : "disabled");
                }));
    }
}
