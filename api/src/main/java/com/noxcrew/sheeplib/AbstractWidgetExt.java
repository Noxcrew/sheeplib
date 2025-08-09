package com.noxcrew.sheeplib;

/**
 * An extension to {@link net.minecraft.client.gui.components.AbstractWidget} to allow for hovering to be disabled.
 */
public interface AbstractWidgetExt {
    /**
     * Returns whether this widget can be hovered over.
     */
    boolean sheeplib$isHoverable();

    /**
     * Sets whether this widget can be hovered over.
     */
    void sheeplib$setHoverable(boolean hoverable);
}
