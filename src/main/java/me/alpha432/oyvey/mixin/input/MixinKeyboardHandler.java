package me.alpha432.oyvey.mixin.input;

import me.alpha432.oyvey.event.impl.input.KeyInputEvent;
import net.minecraft.client.KeyboardHandler;
import net.minecraft.client.input.KeyEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static me.alpha432.oyvey.util.traits.Util.EVENT_BUS;

@Mixin(KeyboardHandler.class)
public class MixinKeyboardHandler {
    @Inject(method = "keyPress", at = @At("TAIL"))
    private void keyPress(long l, int i, KeyEvent keyEvent, CallbackInfo ci) {
        if (i != 1) return;

        EVENT_BUS.post(new KeyInputEvent(keyEvent.key()));
    }
}
