package com.noxcrew.sheeplib.mixin;


import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.noxcrew.sheeplib.AbstractWidgetExt;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

/**
 * Disables hover detection on widgets in unfocussed widgets.
 */
@Mixin(AbstractWidget.class)
public class AbstractWidgetMixin implements AbstractWidgetExt {

    @Unique
    private boolean sheeplib$hoverable = true;

    @Override
    public boolean sheeplib$isHoverable() {
        return sheeplib$hoverable;
    }

    @Override
    public void sheeplib$setHoverable(boolean hoverable) {
        this.sheeplib$hoverable = hoverable;
    }

    /**
     * Wraps requests if an element is being hovered to always be false if not hoverable.
     */
    @WrapOperation(
        method = "render",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/GuiGraphics;containsPointInScissor(II)Z"
        )
    )
    public boolean render(GuiGraphics instance, int i, int j, Operation<Boolean> original) {
        return sheeplib$hoverable ? original.call(instance, i, j) : false;
    }
}
