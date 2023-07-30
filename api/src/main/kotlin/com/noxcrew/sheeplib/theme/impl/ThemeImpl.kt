package com.noxcrew.sheeplib.theme.impl

import com.noxcrew.sheeplib.theme.Theme
import com.noxcrew.sheeplib.util.Icon

/** A basic data class implementation of [Theme.Colors]. */
public data class ColorsImpl(
    override val dialogBackground: Int,
    override val dialogBackgroundAlt: Int = dialogBackground,
    override val widgetBackgroundPrimary: Int,
    override val widgetBackgroundSecondary: Int,
    override val border: Int,
    override val textPrimary: Int,
    override val textSecondary: Int,
    override val positive: Int,
    override val negative: Int,
) : Theme.Colors

/** A data class implementation of [Theme.Icons]. */
public data class IconsImpl(
    override val dropdown: Icon
) : Theme.Icons

/** A data class implementation of [Theme]. */
public data class ThemeImpl(
    override val dimensions: Theme.Dimensions,
    override val colors: Theme.Colors,
    override val buttonStyles: Theme.ButtonStyles,
    override val icons: Theme.Icons,
    override val dialogBorders: Boolean,
): Theme {
    override val theme: ThemeImpl = this
}
