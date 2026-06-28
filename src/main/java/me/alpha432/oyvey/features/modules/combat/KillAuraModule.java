package me.alpha432.oyvey.features.modules.combat;

import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.event.impl.entity.player.TickEvent;
import me.alpha432.oyvey.event.system.Subscribe;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.settings.Setting;
import me.alpha432.oyvey.util.models.Angles;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class KillAuraModule extends Module {
    private final Setting<Float> delay = num("Delay", 1f, 0f, 1f);
    private final Setting<Float> range = num("Range", 3f, 1f, 6f);
    private final Setting<Boolean> onlyWeapon = bool("OnlyWeapon", false);
    private final Setting<Boolean> sync = bool("MovementSync", false);

    public KillAuraModule() {
        super("KillAura", "Attacks nearby players.", Category.COMBAT);
    }

    @Subscribe public void onPreTick(TickEvent.Pre event) {
        if (isOnCooldown())
            return;

        if (onlyWeapon.getValue() && !isHoldingWeapon())
            return;

        Player target = findPlayerTarget();
        if (target == null)
            return;

        Angles angles = Angles.entity(target);
        int priority = 0;

        if (sync.getValue()) {
            OyVey.rotationManager.sync(angles, priority, () -> attack(target));
        } else {
            OyVey.rotationManager.motion(angles, priority, () -> attack(target));
        }
    }

    private void attack(Entity entity) {
        mc.gameMode.attack(mc.player, entity);
        mc.player.swing(InteractionHand.MAIN_HAND);
    }

    private boolean isOnCooldown() {
        float attackDelay = sync.getValue() ? 1 : 0;
        return mc.player.getAttackStrengthScale(attackDelay) < delay.getValue();
    }

    private boolean isHoldingWeapon() {
        ItemStack stack = mc.player.getMainHandItem();
        return stack.is(ItemTags.SWORDS) || stack.is(ItemTags.AXES) || stack.is(ItemTags.SPEARS)
                || stack.is(Items.MACE) || stack.is(Items.TRIDENT);
    }

    private Player findPlayerTarget() {
        Player target = null;
        float min = range.getValue();
        for (Player entity : mc.level.players()) {
            float dist = entity.distanceTo(mc.player);
            if (entity == mc.player || dist > min) continue;
            if (OyVey.friendManager.isFriend(entity)) continue;
            target = entity;
            min = dist;
        }
        return target;
    }
}
