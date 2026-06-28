package me.alpha432.oyvey.features.commands;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.features.commands.argument.ColorArgumentType;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.settings.Setting;
import me.alpha432.oyvey.manager.CommandManager;
import me.alpha432.oyvey.util.chat.ChatUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;

import java.awt.*;

import static com.mojang.brigadier.arguments.BoolArgumentType.getBool;
import static com.mojang.brigadier.arguments.StringArgumentType.getString;
import static com.mojang.brigadier.arguments.StringArgumentType.greedyString;
import static me.alpha432.oyvey.features.commands.argument.ColorArgumentType.getColor;
import static me.alpha432.oyvey.features.commands.argument.EnumArgumentType._enum;
import static me.alpha432.oyvey.features.commands.argument.EnumArgumentType.getEnum;
import static me.alpha432.oyvey.features.commands.argument.NumberArgumentType.*;

public class ModuleCommand extends Command {
    private final Module module;

    public ModuleCommand(Module module) {
        super(module.getName().toLowerCase());
        setDescription("Command line configuration implementation for \"" + module.getName() + "\"");
        this.module = module;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void createArgumentBuilder(LiteralArgumentBuilder<CommandManager> builder) {
        registerDefault(builder);
        for (Setting<?> setting : module.getSettings()) {
            Class<?> type = setting.getDefaultValue().getClass();

            if (Boolean.class.isAssignableFrom(type)) {
                registerBooleanArgument(builder, (Setting<Boolean>) setting);
            } else if (Number.class.isAssignableFrom(type)) {
                registerNumberArgument(builder, (Setting<? extends Number>) setting);
            } else if (Enum.class.isAssignableFrom(type)) {
                registerEnumArgument(builder, (Setting<Enum<?>>) setting);
            } else if (String.class.isAssignableFrom(type)) {
                registerStringArgument(builder, (Setting<String>) setting);
            } else if (Color.class.isAssignableFrom(type)) {
                registerColorArgument(builder, (Setting<Color>) setting);
            }
        }
    }

    private void registerDefault(LiteralArgumentBuilder<CommandManager> builder) {
        builder.executes(ctx -> {
            var message = Component.empty();

            message.append("[ ");
            message.append(Component.literal(module.getName())
                    .withStyle(s -> s.withColor(module.isToggled() ? ChatFormatting.GREEN : ChatFormatting.RED))
                    .withStyle(s -> s.withClickEvent(suggestCommand("toggle " + module.getName())))
            );
            message.append(" ]");

            for (Setting<?> setting : module.getSettings()) {
                message.append("\n");
                message.append(Component.literal(setting.getName() + ": ").withStyle(ChatFormatting.GRAY));
                message.append(Component.literal(setting.getValueAsString()).withStyle(s -> styledValue(setting, s)));
            }

            ChatUtil.sendMessage(message, MessageSignatures.SUCCESS);

            return success();
        });
    }

    private void registerColorArgument(LiteralArgumentBuilder<CommandManager> builder,
                                       Setting<Color> setting) {
        builder.then(literal(setting.getName().toLowerCase())
                .then(argument("value", ColorArgumentType.color())
                        .executes((ctx) -> {
                            setting.setValue(getColor(ctx, "value"));
                            Color value = setting.getValue();
                            return success("Set %s.%s to RGB(%s, %s, %s)",
                                    module.getName(),
                                    setting.getName(),
                                    value.getRed(),
                                    value.getGreen(),
                                    value.getBlue());
                        })));
    }

    private void registerStringArgument(LiteralArgumentBuilder<CommandManager> builder,
                                        Setting<String> setting) {
        builder.then(literal(setting.getName().toLowerCase())
                .then(argument("value", greedyString())
                        .executes((ctx) -> {
                            setting.setValue(getString(ctx, "value"));
                            return settingChangeReturn(setting);
                        })));
    }

    @SuppressWarnings("unchecked")
    private void registerEnumArgument(LiteralArgumentBuilder<CommandManager> builder,
                                      Setting<Enum<?>> setting) {
        Class<Enum<?>> type = (Class<Enum<?>>) setting.getDefaultValue().getClass();
        builder.then(literal(setting.getName().toLowerCase())
                .then(argument("value", _enum(type))
                        .executes((ctx) -> {
                            setting.setValue(getEnum(ctx, "value"));
                            return settingChangeReturn(setting);
                        })));
    }

    @SuppressWarnings("unchecked")
    private <T extends Number> void registerNumberArgument(LiteralArgumentBuilder<CommandManager> builder,
                                                           Setting<T> setting) {
        Class<T> type = (Class<T>) setting.getDefaultValue().getClass();
        builder.then(literal(setting.getName().toLowerCase())
                .then(argument("value", number(type, minMax(setting.getMin(), setting.getMax())))
                        .executes((ctx) -> {
                            setting.setValue(get(type, ctx, "value"));
                            return settingChangeReturn(setting);
                        })));
    }

    private void registerBooleanArgument(LiteralArgumentBuilder<CommandManager> builder,
                                         Setting<Boolean> setting) {
        builder.then(literal(setting.getName().toLowerCase())
                .then(argument("value", BoolArgumentType.bool())
                        .executes((ctx) -> {
                            setting.setValue(getBool(ctx, "value"));
                            return settingChangeReturn(setting);
                        })));
    }

    private int settingChangeReturn(final Setting<?> setting) {
        return success("Set %s.%s to %s", module.getName(), setting.getName(), setting.getValue());
    }

    private static ClickEvent suggestCommand(String command) {
        return new ClickEvent.SuggestCommand(OyVey.commandManager.getCommandPrefix() + command);
    }

    private static Style styledValue(Setting<?> setting, Style style) {
        return switch (setting.getValue()) {
            case Boolean value -> style.withColor(value ? ChatFormatting.GREEN : ChatFormatting.RED);
            case Color value -> style.withColor(value.getRGB());
            case Enum<?> ignored -> style.withColor(ChatFormatting.YELLOW);
            default -> style.withColor(ChatFormatting.WHITE);
        };
    }

    @Override
    public boolean isShown() {
        return false;
    }
}
