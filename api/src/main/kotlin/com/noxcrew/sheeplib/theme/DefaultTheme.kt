@file:Suppress("MagicNumber")

package com.noxcrew.sheeplib.theme

import com.noxcrew.sheeplib.dialog.title.TextTitleWidget
import com.noxcrew.sheeplib.theme.impl.ColorsImpl
import com.noxcrew.sheeplib.theme.impl.IconsImpl
import com.noxcrew.sheeplib.theme.impl.ThemeImpl
import com.noxcrew.sheeplib.util.Icon
import com.noxcrew.sheeplib.util.lighten
import com.noxcrew.sheeplib.util.opaqueColor
import net.minecraft.network.chat.Component
import net.minecraft.resources.Identifier

private val colors = ColorsImpl(
    dialogBackground = 0x2c2c2c.opaqueColor(),

    widgetBackgroundPrimary = 0x404040.opaqueColor(),
    widgetBackgroundSecondary = 0x505050.opaqueColor(),

    border = 0x1a1a1a.opaqueColor(),

    textPrimary = -1,
    textSecondary = 0xa0a0a0.opaqueColor(),

    positive = 0x4d7a4d.opaqueColor(),
    negative = 0x975e5e.opaqueColor(),
)

/** The default theme. */
public val DefaultTheme: ThemeImpl = ThemeImpl(
    Theme.Dimensions(
        buttonWidth = 70,
        buttonHeight = 14,
        paddingInner = 3,
        paddingOuter = 5,
    ),
    colors,
    Theme.ButtonStyles(
        standard = Theme.ButtonStyle(
            ThemedColorReference.WIDGET_BACKGROUND_PRIMARY,
            ThemedColorReference.WIDGET_BACKGROUND_SECONDARY,
            ThemedColorReference.WIDGET_BACKGROUND_SECONDARY,
        ),

        positive = Theme.ButtonStyle(
            ThemedColorReference.POSITIVE,
            // fixme
            StaticColorReference(colors.positive lighten -0.25f),
            ThemedColorReference.WIDGET_BACKGROUND_SECONDARY
        ),

        negative = Theme.ButtonStyle(
            ThemedColorReference.NEGATIVE,
            // fixme
            StaticColorReference(colors.negative lighten -0.25f),
            ThemedColorReference.WIDGET_BACKGROUND_SECONDARY
        ),
    ),
    IconsImpl(
        dropdown = Icon(Identifier.fromNamespaceAndPath("sheeplib", "textures/dropdown.png"), 4, 7),
        close = Icon(
            Identifier.fromNamespaceAndPath("sheeplib", "textures/close.png"),
            5,
            5,
            Identifier.fromNamespaceAndPath("sheeplib", "textures/close_hover.png"),
        )
    ),
    true,
    widgetTitleFactory = {dialog, text, closeable -> TextTitleWidget(dialog, Component.literal(text), closeable) },
)
