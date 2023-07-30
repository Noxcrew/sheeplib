package com.noxcrew.sheeplib.dialog

import net.minecraft.client.gui.components.events.GuiEventListener
import net.minecraft.client.gui.layouts.LayoutElement
import net.minecraft.client.gui.navigation.ScreenRectangle

/**
 * A widget that acts as a title for a [Dialog].
 *
 * FIXME - explain what this means.
 */
public interface DialogTitleWidget: LayoutElement, GuiEventListener {

    /**
     * The widget's parent dialog.
     */
    public val dialog: Dialog

    /**
     * Called when the parent dialog [dialog] is resized.
     */
    public fun onDialogResize() {

    }

    override fun getRectangle(): ScreenRectangle = super<LayoutElement>.getRectangle()
}
