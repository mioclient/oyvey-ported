package me.alpha432.oyvey.features.modules.misc;

import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.features.commands.Command;
import me.alpha432.oyvey.features.modules.Module;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import org.lwjgl.glfw.GLFW;

public class MCF extends Module {
    private boolean pressed;

    public MCF() {
        super("MCF", "Middle click friend", Category.MISC, true, false, false);
    }

    @Override
    public void onTick() {
        if (GLFW.glfwGetMouseButton(mc.getWindow().getHandle(), 2) == 1) {
            if (!pressed) click();
            pressed = true;
        } else {
            pressed = false;
        }
    }

    private void click() {
        Entity targetedEntity = mc.targetedEntity;
        if (!(targetedEntity instanceof PlayerEntity)) return;
        String name = ((PlayerEntity) targetedEntity).getGameProfile().getName();

        if (OyVey.friendManager.isFriend(name)) {
            OyVey.friendManager.removeFriend(name);

            Command.sendMessage("{red} %s has been unfriended.", name);
        } else {
            OyVey.friendManager.addFriend(name);
            Command.sendMessage("{aqua} %s has been friended.", name);
        }
    }
}
