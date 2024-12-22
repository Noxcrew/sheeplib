package com.noxcrew.sheeplib;

/**
 * An extension to {@link net.minecraft.client.gui.components.StringWidget} to allow for shadows to be disabled.
 */
public interface StringWidgetExt {
    /**
     * Returns whether this widget should render text with a shadow.
     * <p>
     * Deprecated - text shadow colour can now be controlled in components. See {@link net.minecraft.network.chat.Style#getShadowColor}.
     */
    @Deprecated(forRemoval = true)
    boolean shouldRenderWithShadow();

    /**
     * Sets whether this widget should render text with a shadow.
     * <p>
     * Deprecated - text shadow colour can now be controlled in components. See {@link net.minecraft.network.chat.Style#getShadowColor}.
     */
    @Deprecated(forRemoval = true)
    void shouldRenderWithShadow(boolean shadow);
}
