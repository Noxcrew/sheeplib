package com.noxcrew.sheeplib.widget

import com.noxcrew.sheeplib.LayoutConstants
import com.noxcrew.sheeplib.dialog.Dialog
import com.noxcrew.sheeplib.layout.linear
import com.noxcrew.sheeplib.theme.Themed
import com.noxcrew.sheeplib.util.withOuterPadding
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.layouts.Layout
import net.minecraft.client.gui.layouts.LinearLayout
import net.minecraft.network.chat.Component

/**
 * A dropdown button widget that displays a list of options in a popup dialog.
 *
 * @param T the type of the options
 * @param parent the parent dialog that the dropdown button belongs to
 * @param options the list of options to be displayed
 * @param width the width of the button, and by extension the dropdown selection
 * @param height the height of the button
 * @param isEnabled whether the button is enabled
 * @param default the default selected option
 * @param displayMapper a function that maps an option to a text component for display
 */
public class DropdownButton<T : Any>(
    private val parent: Dialog,
    private val options: List<T>,
    width: Int = parent.theme.dimensions.buttonWidth,
    height: Int = parent.theme.dimensions.buttonHeight,
    isEnabled: Boolean = true,
    default: T = options.getOrElse(0) { throw IllegalArgumentException("Options list must not be empty") },
    private val displayMapper: (T) -> Component,
) :
    ThemedButton(
        displayMapper(default),
        parent,
        width,
        height,
        isEnabled,
        parent.theme.buttonStyles.standard,
        false,
        true,
        {}) {

    /** The currently selected element. */
    public var selected: T = default
        private set(value) {
            field = value
            message = displayMapper(selected)
        }

    private var selectionPopup: SelectionPopup? = null

    override fun getMessage(): Component = displayMapper(selected)

    override fun maxTextWidth(): Int =
        super.maxTextWidth() - theme.icons.dropdown.width - theme.dimensions.paddingInner

    /** Whether the button has an open selection popup. */
    private fun isOpen() = selectionPopup != null && parent.popup == selectionPopup

    override fun renderWidget(graphics: GuiGraphics, i: Int, j: Int, f: Float) {
        if (!isOpen()) {
            super.renderWidget(graphics, i, j, f)
            theme.icons.dropdown.let {
                it.blit(
                    graphics,
                    x + width - theme.dimensions.paddingInner - it.width,
                    y + theme.dimensions.paddingInner + 2
                )
            }
        }
    }

    override fun onClick(d: Double, e: Double) {
        if (isOpen()) return
        selectionPopup = SelectionPopup(x, y).also {
            parent.popup(it, false)
        }
    }

    /** The selection popup, used to select an element when clicked. */
    private inner class SelectionPopup(x: Int, y: Int) : Dialog(x, y), Themed by withOuterPadding(parent.theme, 0) {

        init {
            init()
        }


        override fun layout(): Layout {
            val buttonHeight = Minecraft.getInstance().font.lineHeight + 3
            this@SelectionPopup.height = buttonHeight * options.size

            return linear(
                this@DropdownButton.width,
                this@SelectionPopup.height,
                LinearLayout.Orientation.VERTICAL
            ) {
                options.forEach {
                    ThemedButton(
                        displayMapper(it),
                        width = this@DropdownButton.width,
                        height = buttonHeight,
                    ) {
                        select(it)
                    }.add(LayoutConstants.CENTRE)
                }
            }
        }

        private fun select(value: T) {
            this@DropdownButton.selected = value
            close()
        }

        override fun mouseClicked(d: Double, e: Double, i: Int): Boolean {
            if (!this@DropdownButton.isHoveredOrFocused and !isMouseOver(d, e)) {
                close()
                return false
            }
            return super.mouseClicked(d, e, i)
        }
    }
}
