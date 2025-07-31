package com.noxcrew.sheeplib.mixin;

import com.noxcrew.sheeplib.DialogContainer;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Adds the {@link DialogContainer} as a render layer.
 */
@Mixin(Gui.class)
public class GuiMixin {
    @Inject(method = "render", at = @At("TAIL"))
    public void render(GuiGraphics graphics, DeltaTracker deltaTracker, CallbackInfo ci) {
        DialogContainer.INSTANCE.render(graphics, deltaTracker);
    }
}
