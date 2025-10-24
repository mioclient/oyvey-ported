package me.alpha432.oyvey.features.modules.player;
import me.alpha432.oyvey.features.modules.Module;
import net.minecraft.item.Items;
import java.util.Random;
public class FastPlace extends Module {
    private Random random = new Random();
    public FastPlace() {
        super("FastPlace", "Reduces item use delay with human-like timing", Category.PLAYER, true, false, false);
    }
    @Override
    public void onUpdate() {
        if (nullCheck()) return;
        if (mc.player.isHolding(Items.EXPERIENCE_BOTTLE)) {
            mc.itemUseCooldown = random.nextInt(6) + 2;
        }
    }
}
