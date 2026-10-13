package me.alpha432.oyvey.features.settings;

import com.mojang.blaze3d.platform.InputConstants;
import me.alpha432.oyvey.util.traits.Util;
import net.minecraft.client.input.KeyEvent;
import org.lwjgl.glfw.GLFW;

public class Bind implements Util {
    private int key;

    public Bind(int key) {
        this.key = key;
    }

    public static Bind keyboard(int key) {
        return new Bind(key);
    }

    public static Bind mouse(int key) {
        return new Bind(-key - 2);
    }

    public static Bind none() {
        return new Bind(-1);
    }

    public int getKey() {
        return this.key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public boolean isEmpty() {
        return this.key == -1;
    }

    public boolean isMouse() {
        return key < -1;
    }

    public String toString() {
        if (this.isEmpty()) return "None";
        if (this.key < -1) return "Mouse " + (-this.key - 1);
        return this.capitalise(InputConstants.getKey(new KeyEvent(this.key, 0, 0)).getName().replace("key.keyboard.", ""));
    }

    public boolean isDown() {
        if (this.isEmpty()) return false;
        if (this.key < -1) return GLFW.glfwGetMouseButton(mc.getWindow().handle(), -this.key - 2) == 1;
        return GLFW.glfwGetKey(mc.getWindow().handle(), this.getKey()) == 1;
    }

    private String capitalise(String str) {
        if (str.isEmpty()) {
            return "";
        }
        return Character.toUpperCase(str.charAt(0)) + (str.length() != 1 ? str.substring(1).toLowerCase() : "");
    }
}