package com.noxcrew.sheeplib.mixin;

import com.noxcrew.noxesium.NoxesiumMod;
import com.noxcrew.sheeplib.DialogContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.Mth;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Noxesium has a setting to completely override the vanilla GUI rendering code
 * to improve performance, which is great, but breaks SheepLib dialog rendering.
 * This mixin just renders dialogs before Noxesium works its magic.
 */
@Mixin(value = Gui.class, priority = 800)
public class NoxesiumCompatGuiMixin {
    @Shadow
    @Final
    private Minecraft minecraft;

    @Inject(
            method = "render",
            at = @At("HEAD")
    )
    public void render(GuiGraphics guiGraphics, float f, CallbackInfo ci) {
        if (NoxesiumMod.getInstance().getConfig().shouldDisableExperimentalPerformancePatches()) return;

        // FIXME: render order is completely off here. not sure if that's my fault or a noxesium thing
        final var window = this.minecraft.getWindow();

        final var mouseX = Mth.floor(minecraft.mouseHandler.xpos() * (double) window.getGuiScaledWidth() / (double) window.getScreenWidth());
        final var mouseY = Mth.floor(minecraft.mouseHandler.ypos() * (double) window.getGuiScaledHeight() / (double) window.getScreenHeight());
        DialogContainer.INSTANCE.render(guiGraphics, mouseX, mouseY, 0);
    }
}
