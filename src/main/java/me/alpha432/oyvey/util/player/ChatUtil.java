package me.alpha432.oyvey.util.player;

import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.features.commands.Command;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;

import static me.alpha432.oyvey.util.traits.Util.mc;

public class ChatUtil {
    public static void sendMessage(Component message) {
        sendSilentMessage(Component.empty()
                .setStyle(Style.EMPTY.withColor(ChatFormatting.GRAY))
                .append("<")
                .append(getClientNameComponent())
                .append(">")
                .append(" ")
                .append(message));
    }

    public static void sendSilentMessage(Component message) {
        if (Command.nullCheck()) {
            return;
        }
        // TODO add silent support ig
        mc.gui.getChat().addMessage(message);
    }
    
    public static Component getClientNameComponent() {
        return Component.empty().withColor(OyVey.colorManager.getColorAsInt()).append("OyVey");
    }
}