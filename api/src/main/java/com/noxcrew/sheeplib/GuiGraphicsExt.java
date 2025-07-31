package com.noxcrew.sheeplib;

/**
 * An extension to {@link net.minecraft.client.gui.GuiGraphics} to allow overriding text opacity.
 */
public interface GuiGraphicsExt {
    /**
     * Returns the current override to text opacity.
     */
    Float sheeplib$getTextOpacityOverride();

    /**
     * Sets an override to apply to the text opacity of all submitted text elements.
     */
    void sheeplib$setTextOpacityOverride(Float override);
}
