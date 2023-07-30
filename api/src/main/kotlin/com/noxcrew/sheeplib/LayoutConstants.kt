package com.noxcrew.sheeplib

import net.minecraft.client.gui.layouts.LayoutSettings

/** Some [LayoutSettings]. */
public object LayoutConstants {
    /** A [LayoutSettings] that's aligned horizontally left and vertically central. */
    public val LEFT: LayoutSettings = LayoutSettings.defaults().alignHorizontallyLeft().alignVerticallyMiddle()

    /** A [LayoutSettings] that's centrally in both directions. */
    public val CENTRE: LayoutSettings = LayoutSettings.defaults().alignHorizontallyCenter().alignVerticallyMiddle()

    /** A [LayoutSettings] that's aligned horizontally right and vertically central. */
    public val RIGHT: LayoutSettings = LayoutSettings.defaults().alignHorizontallyRight().alignVerticallyMiddle()
}
