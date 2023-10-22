package com.noxcrew.sheeplib.mixin;

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
    @SuppressWarnings({"MixinAnnotationTarget", "UnresolvedMixinReference"})
    @Inject(
            method = "render",
            at = @At(
                    value = "NEW",
                    target = "net/minecraft/world/phys/Vec3",
                    ordinal = 0
            )
    )
    public void pushPose(GuiGraphics guiGraphics, CallbackInfo ci) {
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(0.0, 0.0, 200.0);
    }

    @Inject(
            method = "render",
            at = @At("TAIL")
    )
    public void popPose(GuiGraphics guiGraphics, CallbackInfo ci) {
        guiGraphics.pose().popPose();
    }
}
