package me.alpha432.oyvey.features.modules.hud;

import me.alpha432.oyvey.event.impl.Render2DEvent;
import me.alpha432.oyvey.features.modules.client.HudModule;
import me.alpha432.oyvey.features.settings.Setting;

public class Coordinates extends HudModule {
    public Setting<Boolean> nether = bool("Nether", false);

    public Coordinates() {
        super("Coordinates", "Display coordinates", 150, 20);
    }

    @Override
    protected void render(Render2DEvent e) {
        super.render(e);

        if (nullCheck()) return;

        String coordsStr = String.format("X: %d Y: %d Z: %d",
                mc.player.getBlockX(),
                mc.player.getBlockY(),
                mc.player.getBlockZ());

        if (nether.getValue()) {
            int netherX = mc.player.level().dimension().location().getPath().equals("the_nether")
                    ? mc.player.getBlockX() * 8
                    : mc.player.getBlockX() / 8;
            int netherZ = mc.player.level().dimension().location().getPath().equals("the_nether")
                    ? mc.player.getBlockZ() * 8
                    : mc.player.getBlockZ() / 8;
            coordsStr += String.format(" [%d, %d]", netherX, netherZ);
        }

        e.getContext().drawString(mc.font, coordsStr,
                (int) getX(), (int) getY(), -1);

        setWidth(mc.font.width(coordsStr));
        setHeight(mc.font.lineHeight);
    }
}

