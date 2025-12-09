package me.alpha432.oyvey.util.player;

import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.features.commands.Command;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import static me.alpha432.oyvey.util.traits.Util.mc;

public class ChatUtil {

    public static void sendMessage(Component message) {
        MutableComponent text = Component.empty();
        text.append(OyVey.commandManager.getClientMessage()+" ");
        text.append(message);
        sendSilentMessage(text);
    }

    public static void sendSilentMessage(Component message) {
        if (Command.nullCheck()) {
            return;
        }
        // TODO add silent support ig
        mc.gui.getChat().addMessage(message);
    }
}