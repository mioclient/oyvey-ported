package me.alpha432.oyvey.util.player;

import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.features.commands.Command;
import me.alpha432.oyvey.util.TextUtil;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import static me.alpha432.oyvey.util.traits.Util.mc;

public class ChatUtil {

    public static void sendMessage(Text message) {
        MutableText text = Text.empty();
        text.append(OyVey.commandManager.getClientMessage()+" ");
        text.append(message);
        sendSilentMessage(text);
    }

    public static void sendSilentMessage(Text message) {
        if (Command.nullCheck()) {
            return;
        }
        // TODO add silent support ig
        mc.inGameHud.getChatHud().addMessage(message);
    }
}