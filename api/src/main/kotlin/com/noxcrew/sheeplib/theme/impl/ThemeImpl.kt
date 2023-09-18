package com.noxcrew.sheeplib.theme.impl

import com.noxcrew.sheeplib.dialog.Dialog
import com.noxcrew.sheeplib.dialog.title.DialogTitleWidget
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
    override val dropdown: Icon,
    override val close: Icon,
) : Theme.Icons

/** A data class implementation of [Theme]. */
public data class ThemeImpl(
    override val dimensions: Theme.Dimensions,
    override val colors: Theme.Colors,
    override val buttonStyles: Theme.ButtonStyles,
    override val icons: Theme.Icons,
    override val dialogBorders: Boolean,
    private val widgetTitleFactory: (Dialog, String, Boolean) -> DialogTitleWidget?
) : Theme {
    override val theme: ThemeImpl = this
    override fun createTitleWidget(dialog: Dialog, text: String, isCloseable: Boolean): DialogTitleWidget? =
        widgetTitleFactory(dialog, text, isCloseable)
}
