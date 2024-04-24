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
//    @SuppressWarnings("InvalidInjectorMethodSignature")
//    @Inject(
//            method = "render",
//            at = @At(
//                    value = "INVOKE",
//                    target = "Lnet/minecraft/client/gui/components/ChatComponent;render(Lnet/minecraft/client/gui/GuiGraphics;III)V"
//            ),
//            locals = LocalCapture.CAPTURE_FAILHARD)
//    public void render(GuiGraphics guiGraphics, float f, CallbackInfo ci, Window window, Scoreboard scoreboard, int o, int q) {
//        DialogContainer.INSTANCE.render(guiGraphics, o, q, 0);
//    }

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
