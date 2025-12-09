package me.alpha432.oyvey.manager;

import me.alpha432.oyvey.util.MathUtil;
import me.alpha432.oyvey.util.traits.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public class RotationManager implements Util {
    private float yaw;
    private float pitch;

    public void updateRotations() {
        this.yaw = mc.player.getYRot();
        this.pitch = mc.player.getXRot();
    }

    public void restoreRotations() {
        mc.player.setYRot(yaw);
        mc.player.yHeadRot = yaw;
        mc.player.setXRot(pitch);
    }

    public void setPlayerRotations(float yaw, float pitch) {
        mc.player.setYRot(yaw);
        mc.player.yHeadRot = yaw;
        mc.player.setXRot(pitch);
    }

    public void setPlayerYaw(float yaw) {
        mc.player.setYRot(yaw);
        mc.player.yHeadRot = yaw;
    }

    public void lookAtPos(BlockPos pos) {
        float[] angle = MathUtil.calcAngle(mc.player.getEyePosition(), new Vec3((float) pos.getX() + 0.5f, (float) pos.getY() + 0.5f, (float) pos.getZ() + 0.5f));
        this.setPlayerRotations(angle[0], angle[1]);
    }

    public void lookAtVec3d(Vec3 vec3d) {
        float[] angle = MathUtil.calcAngle(mc.player.getEyePosition(), new Vec3(vec3d.x, vec3d.y, vec3d.z));
        this.setPlayerRotations(angle[0], angle[1]);
    }

    public void lookAtVec3d(double x, double y, double z) {
        Vec3 vec3d = new Vec3(x, y, z);
        this.lookAtVec3d(vec3d);
    }

    public void lookAtEntity(Entity entity) {
        float[] angle = MathUtil.calcAngle(mc.player.getEyePosition(), entity.getEyePosition());
        this.setPlayerRotations(angle[0], angle[1]);
    }

    public void setPlayerPitch(float pitch) {
        mc.player.setXRot(pitch);
    }

    public float getYaw() {
        return this.yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public float getPitch() {
        return this.pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

}
