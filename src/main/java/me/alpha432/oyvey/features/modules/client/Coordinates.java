package me.alpha432.oyvey.features.modules.client;

import com.google.common.eventbus.Subscribe;
import me.alpha432.oyvey.event.impl.Render2DEvent;
import me.alpha432.oyvey.features.settings.Setting;

public class Coordinates extends HudModule {
    public Setting<Boolean> nether = bool("Nether", false);

    public Coordinates() {
        super("Coordinates", "Display coordinates", 150, 20);
    }

    @Override
    @Subscribe
    public void onRender2D(Render2DEvent e) {
        super.onRender2DHud(e);

        if (fullNullCheck()) return;

        String coordsStr = String.format("X: %d Y: %d Z: %d",
                mc.player.getBlockX(),
                mc.player.getBlockY(),
                mc.player.getBlockZ());

        if (nether.getValue()) {
            int netherX = mc.player.getWorld().getRegistryKey().getValue().getPath().equals("the_nether")
                    ? mc.player.getBlockX() * 8
                    : mc.player.getBlockX() / 8;
            int netherZ = mc.player.getWorld().getRegistryKey().getValue().getPath().equals("the_nether")
                    ? mc.player.getBlockZ() * 8
                    : mc.player.getBlockZ() / 8;
            coordsStr += String.format(" [%d, %d]", netherX, netherZ);
        }

        e.getContext().drawTextWithShadow(mc.textRenderer, coordsStr,
                (int) getX(), (int) getY(), -1);

        setWidth(mc.textRenderer.getWidth(coordsStr));
        setHeight(mc.textRenderer.fontHeight);
    }
}

