package me.alpha432.oyvey.features.modules.movement;

import me.alpha432.oyvey.event.impl.entity.player.MoveEvent;
import me.alpha432.oyvey.event.system.Subscribe;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.settings.Setting;
import me.alpha432.oyvey.util.MovementUtil;
import net.minecraft.world.entity.MoverType;
import org.joml.Vector2f;

public class FastAccelModule extends Module {
    private final Setting<Float> speed = num("Speed", 1f, 0.5f, 2f);
    private final Setting<Boolean> slowness = bool("Slowness", true);

    public FastAccelModule() {
        super("FastAccel", "Makes the player accelerate faster while walking", Category.MOVEMENT);
    }

    @Subscribe
    private void onMove(MoveEvent event) {
        if (nullCheck() || event.getType() != MoverType.SELF) return;
        if (mc.player.isInLiquid() || mc.player.isFallFlying()
                || mc.player.getAbilities().flying || mc.player.isSpectator()) return;

        double speed = MovementUtil.SPRINT_SPEED * this.speed.getValue() * MovementUtil.getSpeedFactor(true, slowness.getValue());

        if (event.getMovement().horizontalDistance() > speed) {
            speed = event.getMovement().horizontalDistance();
        }

        Vector2f motion = MovementUtil.calcMoveVector(mc.player.getYRot(), speed);
        event.setMovement(motion.x, event.getMovement().y, motion.y);
    }
}
