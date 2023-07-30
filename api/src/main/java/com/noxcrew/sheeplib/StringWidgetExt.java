package com.noxcrew.sheeplib;

/**
 * An extension to {@link net.minecraft.client.gui.components.StringWidget} to allow for shadows to be disabled.
 */
public interface StringWidgetExt {
    /**
     * Returns whether this widget should render text with a shadow.
     */
    boolean shouldRenderWithShadow();

    /**
     * Sets whether this widget should render text with a shadow.
     */
    void shouldRenderWithShadow(boolean shadow);
}
