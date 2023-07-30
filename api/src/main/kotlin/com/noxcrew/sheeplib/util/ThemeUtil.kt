package com.noxcrew.sheeplib.util

import com.noxcrew.sheeplib.theme.Theme

public fun withOuterPadding(parentTheme: Theme, padding: Int): Theme = object : Theme by parentTheme {
    override val theme: Theme = this

    override val dimensions: Theme.Dimensions = parentTheme.dimensions.copy(paddingOuter = padding)
}
