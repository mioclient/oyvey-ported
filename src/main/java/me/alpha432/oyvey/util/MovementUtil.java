package me.alpha432.oyvey.util;

import me.alpha432.oyvey.util.traits.Util;
import net.minecraft.client.player.ClientInput;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Vector2f;

public class MovementUtil implements Util {
    public static final double SPRINT_SPEED = 0.2873;

    public static Vector2f calcMoveVector(float yaw, Vector2f input, double speed) {
        Vector2f direction = new Vector2f(input);

        if (direction.lengthSquared() < 1.0E-8f) {
            return direction.zero();
        }

        direction.normalize();

        float rad = (float) Math.toRadians(yaw);
        float sin = (float) Math.sin(rad);
        float cos = (float) Math.cos(rad);

        float x = (direction.x * cos - direction.y * sin) * (float) speed;
        float z = (direction.y * cos + direction.x * sin) * (float) speed;

        return direction.set(x, z);
    }

    public static Vector2f calcMoveVector(float yaw, ClientInput input, double speed) {
        return calcMoveVector(yaw, new Vector2f(input.getMoveVector().x, input.getMoveVector().y), speed);
    }

    public static Vector2f calcMoveVector(float yaw, double speed) {
        return calcMoveVector(yaw, mc.player.input, speed);
    }

    public static double getSpeedFactor(boolean swiftness, boolean slowness) {
        double factor = 1.0;

        MobEffectInstance speedEffect = mc.player.getEffect(MobEffects.SPEED);
        if (swiftness && speedEffect != null) {
            factor *= 1.0 + 0.2 * (speedEffect.getAmplifier() + 1);
        }

        MobEffectInstance slowEffect = mc.player.getEffect(MobEffects.SLOWNESS);
        if (slowness && slowEffect != null) {
            factor *= Math.max(0.0, 1.0 - 0.15 * (slowEffect.getAmplifier() + 1));
        }

        return factor;
    }

    public static double getJumpBoost() {
        return mc.player.getJumpBoostPower();
    }

    public static double getJumpSpeed() {
        return mc.player.getAttributeValue(Attributes.JUMP_STRENGTH) * getBlockJumpFactor(mc.player) + getJumpBoost();
    }

    public static double getJumpHeight() {
        return mc.player.getAttributeValue(Attributes.JUMP_STRENGTH) + getJumpBoost();
    }

    private static float getBlockJumpFactor(LivingEntity entity) {
        BlockState inside = entity.level().getBlockState(entity.blockPosition());
        float factor = inside.getBlock().getJumpFactor();
        if (factor != 1.0f) return factor;
        return entity.level().getBlockState(entity.getBlockPosBelowThatAffectsMyMovement()).getBlock().getJumpFactor();
    }
}
