package com.noxcrew.sheeplib.widget

import com.noxcrew.sheeplib.LayoutConstants
import com.noxcrew.sheeplib.dialog.Dialog
import com.noxcrew.sheeplib.layout.linear
import com.noxcrew.sheeplib.theme.Themed
import com.noxcrew.sheeplib.util.withOuterPadding
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.StringWidget
import net.minecraft.client.gui.layouts.Layout
import net.minecraft.client.gui.layouts.LinearLayout
import net.minecraft.client.input.MouseButtonEvent
import net.minecraft.network.chat.Component
import kotlin.math.roundToInt
import kotlin.math.sign

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

    override fun onClick(mouseButtonEvent: MouseButtonEvent, bl: Boolean) {
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

        private var scroll = 0
        private var pageSize = 0

        override fun renderBackground(graphics: GuiGraphics) {
            if (pageSize >= options.size) return
            val scrollbarHeight = (height * pageSize.toFloat() / options.size).roundToInt()
            val scrollbarY = y + ((height - scrollbarHeight) * scroll / (options.size - pageSize))

            graphics.fill(
                x + width + 1,
                scrollbarY,
                x + width + 2,
                scrollbarY + scrollbarHeight,
            theme.colors.textSecondary
            )
        }

        override fun layout(): Layout {
            val buttonHeight = Minecraft.getInstance().font.lineHeight + 3
            pageSize = (Minecraft.getInstance().window.guiScaledHeight - BOTTOM_EDGE_DEAD_ZONE) / buttonHeight

            this@SelectionPopup.height = buttonHeight * options.size.coerceAtMost(pageSize)
            val isScrolling = options.size > pageSize

            return linear(LinearLayout.Orientation.VERTICAL) {
                options
                    .let {
                        if (isScrolling) it.drop(scroll).take(pageSize) else it
                    }
                    .forEach {
                        ThemedButton(
                            displayMapper(it),
                            width = this@DropdownButton.width,
                            height = buttonHeight,
                        ) {
                            select(it)
                        }.add(LayoutConstants.LEFT)
                    }
            }
        }

        private fun select(value: T) {
            this@DropdownButton.selected = value
            close()
        }

        override fun mouseClicked(mouseButtonEvent: MouseButtonEvent, bl: Boolean): Boolean {
            if (!this@DropdownButton.isHoveredOrFocused and !isMouseOver(mouseButtonEvent.x, mouseButtonEvent.y)) {
                close()
                return false
            }
            return super.mouseClicked(mouseButtonEvent, bl)
        }

        override fun mouseScrolled(d: Double, e: Double, f: Double, g: Double): Boolean {
            if (this@DropdownButton.isHoveredOrFocused) {
                val oldScroll = scroll
                scroll = (scroll - g.sign.toInt()).coerceIn(0, (options.size - pageSize).coerceAtLeast(0))
                if (oldScroll != scroll) init()
                return true
            }
            return super.mouseScrolled(d, e, f, g)
        }
    }
}
