package com.noxcrew.sheeplib.mixin;

import com.mojang.blaze3d.platform.Window;
import com.noxcrew.sheeplib.DialogContainer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.scores.Scoreboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

/**
 * Renders the [DialogContainerScreen].
 */
@Mixin(Gui.class)
public class GuiMixin {
    @SuppressWarnings("InvalidInjectorMethodSignature")
    @Inject(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/components/ChatComponent;render(Lnet/minecraft/client/gui/GuiGraphics;III)V"
            ),
            locals = LocalCapture.CAPTURE_FAILHARD)
    public void render(GuiGraphics guiGraphics, float f, CallbackInfo ci, Window window, Scoreboard scoreboard, int o, int q) {
        DialogContainer.INSTANCE.render(guiGraphics, o, q, 0);
    }
}
