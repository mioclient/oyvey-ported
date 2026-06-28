package me.alpha432.oyvey.ducks.render;

import net.minecraft.client.GuiMessage;
import net.minecraft.network.chat.MessageSignature;

public interface IChatComponent {
    void oyvey$addMessage(GuiMessage message);
    void oyvey$removeMessage(MessageSignature signature);
}