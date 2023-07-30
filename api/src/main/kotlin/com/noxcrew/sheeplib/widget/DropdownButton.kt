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

public class DropdownButton<T : Any>(
    width: Int,
    height: Int,
    private val parent: Dialog,
    private val options: List<T>,
    isEnabled: Boolean = true,
    default: T = options.getOrElse(0) { throw IllegalArgumentException("Options list must not be empty") },
    private val displayMapper: (T) -> Component,
) :
    ThemedButton(
        width,
        height,
        displayMapper(default),
        isEnabled,
        parent,
        parent.theme.buttonStyles.standard,
        false,
        true,
        {}) {

    public var selected: T = default
        private set(value) {
            field = value
            message = displayMapper(selected)
        }

    private var selectionPopup: SelectionPopup? = null

    override fun getMessage(): Component = displayMapper(selected)

    override fun maxTextWidth(): Int =
        super.maxTextWidth() - theme.icons.dropdown.width - theme.dimensions.paddingInner

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
                    ThemedButton(this@DropdownButton.width, buttonHeight, displayMapper(it)) { select(it) }.add(LayoutConstants.CENTRE)
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
