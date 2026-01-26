package me.alpha432.oyvey.features.modules.player;

import me.alpha432.oyvey.event.impl.network.PacketEvent;
import me.alpha432.oyvey.event.system.Subscribe;
import me.alpha432.oyvey.features.modules.Module;
import net.minecraft.network.protocol.game.ClientboundExplodePacket;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;

public class VelocityModule extends Module {
    public VelocityModule() {
        super("Velocity", "Removes velocity from explosions and entities", Category.PLAYER);
    }

    @Subscribe
    private void onPacketReceive(PacketEvent.Receive event) {
        if (event.getPacket() instanceof ClientboundSetEntityMotionPacket || event.getPacket() instanceof ClientboundExplodePacket)
            event.cancel();
    }
}
