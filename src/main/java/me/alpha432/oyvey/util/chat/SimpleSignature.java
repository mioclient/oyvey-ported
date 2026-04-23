package me.alpha432.oyvey.util.chat;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SimpleSignature implements Signature {
    private static final MessageDigest MESSAGE_DIGEST;

    private final byte[] signature;

    SimpleSignature(byte[] signature) {
        this.signature = signature;
    }

    public static SimpleSignature from(String content) {
        byte[] sig = allocateBuf()
                .put((byte) 1)
                .put(sha256(content))
                .array();
        return new SimpleSignature(sig);
    }

    public static SimpleSignature fromInt(int id) {
        byte[] sig = allocateBuf()
                .put((byte) 2)
                .putInt(id)
                .array();
        return new SimpleSignature(sig);
    }

    public static SimpleSignature fromLong(long id) {
        byte[] sig = allocateBuf()
                .put((byte) 3)
                .putLong(id)
                .array();
        return new SimpleSignature(sig);
    }

    @Override
    public byte[] getByteSignature() {
        return signature;
    }

    private static byte[] sha256(String content) {
        return MESSAGE_DIGEST.digest(content.getBytes(StandardCharsets.UTF_8));
    }

    private static ByteBuffer allocateBuf() {
        return ByteBuffer.allocate(256);
    }

    static {
        try {
            MESSAGE_DIGEST = MessageDigest.getInstance("SHA-512");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
