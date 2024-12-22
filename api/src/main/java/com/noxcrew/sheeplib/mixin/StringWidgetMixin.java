package com.noxcrew.sheeplib.mixin;


import com.noxcrew.sheeplib.StringWidgetExt;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Implements {@link StringWidgetExt} on {@link StringWidget}.
 */
@Mixin(StringWidget.class)
@SuppressWarnings("removal") // This mixin implements the offending method and will be removed with it.
public class StringWidgetMixin implements StringWidgetExt {

    @Unique
    private boolean shouldRenderWithShadow = true;

    @Override
    public boolean shouldRenderWithShadow() {
        return shouldRenderWithShadow;
    }

    @Override
    public void shouldRenderWithShadow(boolean shadow) {
        shouldRenderWithShadow = shadow;
    }

    /**
     * Redirects {@link GuiGraphics#drawString(Font, Component, int, int, int)}
     * to {@link GuiGraphics#drawString(Font, Component, int, int, int, boolean)}
     * to inject {@link StringWidgetExt#shouldRenderWithShadow()}.
     */
    @Redirect(
            method = "renderWidget",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/GuiGraphics;drawString(Lnet/minecraft/client/gui/Font;Lnet/minecraft/util/FormattedCharSequence;III)I"
            )
    )
    public int render(GuiGraphics instance, Font font, FormattedCharSequence formattedCharSequence, int i, int j, int k) {
        return instance.drawString(font, formattedCharSequence, i, j, k, shouldRenderWithShadow());
    }

}
