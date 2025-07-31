package com.noxcrew.sheeplib.mixin;


import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.noxcrew.sheeplib.GuiGraphicsExt;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.ARGB;
import net.minecraft.util.FormattedCharSequence;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

/**
 * Modifies string drawing to have reduced opacity if requested.
 */
@Mixin(GuiGraphics.class)
public class GuiGraphicsMixin implements GuiGraphicsExt {

    @Unique
    private Float sheeplib$textOpacityOverride = null;

    @Override
    public Float sheeplib$getTextOpacityOverride() {
        return sheeplib$textOpacityOverride;
    }

    @Override
    public void sheeplib$setTextOpacityOverride(Float override) {
        this.sheeplib$textOpacityOverride = override;
    }

    /**
     * Wraps requests if an element is being hovered to always be false if not hoverable.
     */
    @WrapMethod(method = "drawString(Lnet/minecraft/client/gui/Font;Lnet/minecraft/util/FormattedCharSequence;IIIZ)V")
    public void drawString(Font font, FormattedCharSequence formattedCharSequence, int i, int j, int k, boolean bl, Operation<Void> original) {
        if (sheeplib$textOpacityOverride == null) {
            original.call(font, formattedCharSequence, i, j, k, bl);
        } else {
            var originalAlpha = ARGB.alphaFloat(k);
            var newAlpha = originalAlpha * sheeplib$textOpacityOverride;
            var newColor = ARGB.color(ARGB.as8BitChannel(newAlpha), k);
            original.call(font, formattedCharSequence, i, j, newColor, bl);
        }
    }
}
