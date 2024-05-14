package com.noxcrew.sheeplib.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.noxcrew.sheeplib.DialogContainer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.LayeredDraw;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/**
 * Adds the {@link DialogContainer} as a render layer.
 */
@Mixin(Gui.class)
public class GuiMixin {
    @ModifyExpressionValue(
            method="<init>",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/LayeredDraw;add(Lnet/minecraft/client/gui/LayeredDraw$Layer;)Lnet/minecraft/client/gui/LayeredDraw;",
                    ordinal = 11
            )
    )
    public LayeredDraw addRenderLayer(LayeredDraw original) {
        return original.add(DialogContainer.INSTANCE);
    }

}
