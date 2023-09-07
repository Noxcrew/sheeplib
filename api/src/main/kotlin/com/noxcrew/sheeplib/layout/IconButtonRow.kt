package com.noxcrew.sheeplib.layout

import com.noxcrew.sheeplib.theme.Themed
import com.noxcrew.sheeplib.util.TextButtonCollectionBuilder
import com.noxcrew.sheeplib.widget.ThemedButton

/**
 * Populates a [row] with small square buttons, or a new row at the bottom of the grid if not specified.
 * Each button will occupy its own column, staring from [firstColumn].
 *
 * Buttons should contain a single character, possibly a Unicode symbol/emoji.
 * They may have hover text, which will be shown when the button is hovered over, as per [ThemedButton].
 */
public fun GridLayoutBuilder.iconButtonRow(
    theme: Themed,
    row: Int = lastRow + 1,
    firstColumn: Int = 0,
    builder: TextButtonCollectionBuilder<() -> Unit>.() -> Unit
): Int {
    // +1 works better for icons with an odd width
    val width = theme.theme.dimensions.buttonHeight + 1
    val buttons = TextButtonCollectionBuilder<() -> Unit>().apply(builder)
    buttons.elements.forEachIndexed { index, (text, action) ->
        ThemedButton(
            text,
            width = width,
            centreText = true,
            scrollText = false,
            clickHandler = action
        ).at(row, firstColumn + index)
    }
    return buttons.elements.size
}
