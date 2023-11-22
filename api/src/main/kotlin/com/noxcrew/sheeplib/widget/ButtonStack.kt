package com.noxcrew.sheeplib.widget

import com.noxcrew.sheeplib.CompoundWidget
import com.noxcrew.sheeplib.layout.LinearLayout
import com.noxcrew.sheeplib.theme.StaticColorReference
import com.noxcrew.sheeplib.theme.Theme
import com.noxcrew.sheeplib.theme.Themed
import com.noxcrew.sheeplib.theme.ThemedColorReference
import com.noxcrew.sheeplib.util.TextButtonCollectionBuilder
import com.noxcrew.sheeplib.util.withOuterPadding
import net.minecraft.client.gui.layouts.Layout
import net.minecraft.client.gui.layouts.LinearLayout
import net.minecraft.network.chat.Component

/**
 * A vertical stack of uniform buttons, ordered from top to bottom.
 *
 * @param width the width the widget, and of each button
 * @param lineHeight the height of a single button
 * @param theme the buttons' theme
 * @param entries a list of text entries and callback handlers to create buttons from
 */
public class ButtonStack(
    width: Int,
    lineHeight: Int,
    theme: Themed,
    entries: List<Pair<Component, () -> Unit>>
) : CompoundWidget(0, 0, width, lineHeight * entries.size),
    Themed by theme {

    private companion object {
        private val buttonStyle = Theme.ButtonStyle(
            StaticColorReference(0),
            ThemedColorReference.WIDGET_BACKGROUND_SECONDARY,
            ThemedColorReference.WIDGET_BACKGROUND_SECONDARY,
        )
    }

    override val layout: Layout = LinearLayout(
        LinearLayout.Orientation.VERTICAL,
        0,
    ) {
        entries.forEach { (text, action) ->
            +ThemedButton(
                text,
                withOuterPadding(theme.theme, 0),
                width = width,
                height = lineHeight,
                style = buttonStyle,
                clickHandler = action
            )
        }
    }

    init {
        layout.arrangeElements()
        layout.visitWidgets(this::addChild)
    }
}

/**
 * A vertical stack of uniform buttons, ordered from top to bottom.
 *
 * @param width the width the widget, and of each button
 * @param lineHeight the height of a single button
 * @param theme the buttons' theme
 * @param builder a builder for buttons
 */
public inline fun ButtonStack(
    theme: Themed,
    width: Int,
    lineHeight: Int,
    builder: (TextButtonCollectionBuilder<() -> Unit>).() -> Unit
): ButtonStack = ButtonStack(
    width,
    lineHeight,
    theme,
    TextButtonCollectionBuilder<() -> Unit>().apply(builder).elements
)
