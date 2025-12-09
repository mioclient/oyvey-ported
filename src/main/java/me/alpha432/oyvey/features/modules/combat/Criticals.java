package me.alpha432.oyvey.features.modules.combat;

import me.alpha432.oyvey.event.impl.PacketEvent;
import me.alpha432.oyvey.event.system.Subscribe;
import me.alpha432.oyvey.features.modules.Module;
import net.minecraft.network.protocol.game.ServerboundInteractPacket;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;

public class Criticals extends Module {
    public Criticals() {
        super("Criticals", "Makes you do critical hits", Category.COMBAT);
    }

    @Subscribe
    private void onPacketSend(PacketEvent.Send event) {
        if (event.getPacket() instanceof ServerboundInteractPacket packet && packet.action.getType() == ServerboundInteractPacket.ActionType.ATTACK) {
            Entity entity = mc.level.getEntity(packet.entityId);
            if (entity == null
                    || entity instanceof EndCrystal
                    || !mc.player.onGround()
                    || !(entity instanceof LivingEntity)) return;

            boolean bl = mc.player.horizontalCollision;
            mc.player.connection.send(new ServerboundMovePlayerPacket.Pos(mc.player.getX(), mc.player.getY() + 0.1f, mc.player.getZ(), false, bl));
            mc.player.connection.send(new ServerboundMovePlayerPacket.Pos(mc.player.getX(), mc.player.getY(), mc.player.getZ(), false, bl));
            mc.player.crit(entity);
        }
    }

    @Override
    public String getDisplayInfo() {
        return "Packet";
    }
}
