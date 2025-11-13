package me.alpha432.oyvey.mixin;

import me.alpha432.oyvey.event.impl.KeyEvent;
import net.minecraft.client.Keyboard;
import net.minecraft.client.input.KeyInput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static me.alpha432.oyvey.util.traits.Util.EVENT_BUS;

@Mixin(Keyboard.class)
public class MixinKeyboard {
    @Inject(method = "onKey", at = @At("TAIL"), cancellable = true)
    private void onKey(long window, int action, KeyInput input, CallbackInfo ci) {
        if (action != 1) return;
        KeyEvent event = new KeyEvent(input.key());
        EVENT_BUS.post(event);
        if (event.isCancelled()) ci.cancel();
    }
}
