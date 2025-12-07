package com.noxcrew.sheeplib.mixin;

import com.noxcrew.sheeplib.DialogContainer;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.ChatScreen;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Renders the {@link DialogContainer}.
 */
@Mixin(Gui.class)
public class GuiMixin {
    @Shadow
    @Final
    private Minecraft minecraft;

    @Inject(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/Gui;renderTabList(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/client/DeltaTracker;)V"
            ))
    public void render(GuiGraphics graphics, DeltaTracker deltaTracker, CallbackInfo ci) {
        // Only render when not in a chat screen, otherwise chat is rendered over the top of dialogs.
        if (!(this.minecraft.screen instanceof ChatScreen)) {
            DialogContainer.INSTANCE.render(graphics, 0, 0, deltaTracker.getGameTimeDeltaTicks());
        }
    }
}
