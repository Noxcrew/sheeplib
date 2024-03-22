package com.noxcrew.sheeplib.mixin;

import com.noxcrew.sheeplib.DialogContainer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.SubtitleOverlay;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Increases the subtitle overlay's Z-axis to render on top of SheepLib dialogs.
 */
@Mixin(SubtitleOverlay.class)
public class SubtitleOverlayMixin {
    @Inject(
            method = "render",
            at = @At("HEAD")
    )
    public void pushPose(GuiGraphics guiGraphics, CallbackInfo ci) {
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(0.0, 0.0, DialogContainer.zIndexUse$sheeplib());
    }

    @Inject(
            method = "render",
            at = @At("TAIL")
    )
    public void popPose(GuiGraphics guiGraphics, CallbackInfo ci) {
        guiGraphics.pose().popPose();
    }
}
