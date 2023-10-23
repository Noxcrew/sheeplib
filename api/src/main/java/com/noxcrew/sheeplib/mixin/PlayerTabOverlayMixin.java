package com.noxcrew.sheeplib.mixin;

import com.noxcrew.sheeplib.DialogContainer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.PlayerTabOverlay;
import net.minecraft.world.scores.Objective;
import net.minecraft.world.scores.Scoreboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Increases the player tab overlay's Z-axis to render on top of SheepLib dialogs.
 */
@Mixin(PlayerTabOverlay.class)
public class PlayerTabOverlayMixin {

    @Inject(
            method = "render",
            at = @At("HEAD")
    )
    public void pushPose(GuiGraphics guiGraphics, int i, Scoreboard scoreboard, Objective objective, CallbackInfo ci) {
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(0.0, 0.0, DialogContainer.zIndexUse$sheeplib());
    }

    @Inject(
            method = "render",
            at = @At("TAIL")
    )
    public void popPose(GuiGraphics guiGraphics, int i, Scoreboard scoreboard, Objective objective, CallbackInfo ci) {
        guiGraphics.pose().popPose();
    }
}
