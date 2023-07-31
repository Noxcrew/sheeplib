package com.noxcrew.sheeplib.layout

import net.minecraft.client.gui.layouts.LinearLayout

/** A [LinearLayout] with a manually settable width and height. */
public class AdjustableLinearLayout(x: Int, y: Int, width: Int, height: Int, orientation: Orientation) :
    LinearLayout(x, y, width, height, orientation) {

    /** Sets the layout's width. */
    public fun setWidth(width: Int) {
        this.width = width
    }

    /** Sets the layout's height. */
    public fun setHeight(height: Int) {
        this.height = height
    }
}
