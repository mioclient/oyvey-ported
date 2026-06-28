package me.alpha432.oyvey.features.commands;

import me.alpha432.oyvey.util.chat.Signature;
import me.alpha432.oyvey.util.chat.SimpleSignature;

public class MessageSignatures {
    public static final Signature GENERAL = SimpleSignature.fromLong(-1L);
    public static final Signature SUCCESS = SimpleSignature.fromLong(0L);
    public static final Signature FAIL    = SimpleSignature.fromLong(1L);
}