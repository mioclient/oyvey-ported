package me.alpha432.oyvey.util.chat;

import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.ducks.render.IChatComponent;
import me.alpha432.oyvey.features.commands.Command;
import me.alpha432.oyvey.util.TextUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.GuiMessage;
import net.minecraft.client.GuiMessageTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MessageSignature;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;

import static me.alpha432.oyvey.util.traits.Util.mc;

public class ChatUtil {
    public static MutableComponent prefix() {
        return withPrefix((Component) null);
    }

    public static MutableComponent withPrefix(String string) {
        return withPrefix(string, new Object[0]);
    }

    public static MutableComponent withPrefix(String string, Object... o) {
        return withPrefix(TextUtil.text(string, o));
    }

    public static MutableComponent withPrefix(Component component) {
        MutableComponent text = Component.empty()
                .setStyle(Style.EMPTY.withColor(ChatFormatting.GRAY))
                .append(Component.literal("<"))
                .append(getClientNameComponent())
                .append(Component.literal(">"));
        if (component != null) text.append(" ").append(component);
        return text;
    }

    public static void sendMessage(Component message, Signature identifier) {
        sendClientSideMessage(withPrefix(message), identifier);
    }

    public static void sendClientSideMessage(Component message, Signature sig) {
        if (Command.nullCheck()) return;

        IChatComponent chat = (IChatComponent) mc.gui.getChat();
        MessageSignature signature = new MessageSignature(sig.getByteSignature());
        chat.oyvey$removeMessage(signature);
        chat.oyvey$addMessage(new GuiMessage(mc.gui.getGuiTicks(), message, signature, getMessageTag()));
    }

    public static Component getClientNameComponent() {
        return Component.empty().withColor(OyVey.colorManager.getColorAsInt()).append("OyVey");
    }

    private static GuiMessageTag getMessageTag() {
        return new GuiMessageTag(OyVey.colorManager.getColorAsInt(), null, null, null);
    }
}