package com.noxcrew.sheeplib.mixin;

import com.mojang.blaze3d.platform.Window;
import com.noxcrew.sheeplib.DialogContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ComponentPath;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.CommonColors;
import net.minecraft.world.scores.Scoreboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

/**
 * Renders the [DialogContainerScreen].
 */
@Mixin(Gui.class)
public abstract class GuiMixin {

    @Shadow public abstract Font getFont();

    @Unique
    private int drawContainerFocus(ComponentPath componentPath, GuiGraphics guiGraphics, int y) {
        final var font = Minecraft.getInstance().font;
        int i = y;
        for (
                var el = componentPath;
                el != null;
                el = (el instanceof ComponentPath.Path path) ? path.childPath() : null, i += font.lineHeight
        ) {
            guiGraphics.drawString(font, el.component().toString(), 5, i, CommonColors.WHITE);
        }
        return i;
    }

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

        int y = 5;

        // Debug - draw focus path on screen
        final var screen = Minecraft.getInstance().screen;
        if (screen != null) {
            y = drawContainerFocus(screen.getCurrentFocusPath(), guiGraphics, y);
        }

        drawContainerFocus(DialogContainer.INSTANCE.getCurrentFocusPath(), guiGraphics, y + getFont().lineHeight);
    }
}
