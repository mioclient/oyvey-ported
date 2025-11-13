package me.alpha432.oyvey.mixin;

import me.alpha432.oyvey.event.impl.MouseEvent;
import net.minecraft.client.Mouse;
import net.minecraft.client.input.MouseInput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static me.alpha432.oyvey.util.traits.Util.EVENT_BUS;

@Mixin(Mouse.class)
public class MixinMouse {
    @Inject(method = "onMouseButton", at = @At("HEAD"), cancellable = true)
    private void onMouseButton(long window, MouseInput input, int action, CallbackInfo ci) {
        MouseEvent event = new MouseEvent(input.button(), action);
        EVENT_BUS.post(event);
        if (event.isCancelled()) {
            ci.cancel();
        }
    }
}

