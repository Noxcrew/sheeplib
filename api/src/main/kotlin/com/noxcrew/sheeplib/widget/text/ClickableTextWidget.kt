package com.noxcrew.sheeplib.widget.text

import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Font
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.StringWidget
import net.minecraft.network.chat.Component

/**
 * A text widget that renders a single line of text, correctly handling hover and click.
 *
 * @param component the text component to render
 * @param font the font to render the text with
 */
public class ClickableTextWidget(component: Component, font: Font) : StringWidget(component, font) {

    init {
        active = true
    }

    override fun onClick(d: Double, e: Double) {
        Minecraft.getInstance().screen?.handleComponentClicked(message.styleAtMouseX(d.toInt()))
    }

    override fun renderWidget(guiGraphics: GuiGraphics, i: Int, j: Int, f: Float) {
        super.renderWidget(guiGraphics, i, j, f)
        if (isHovered()) guiGraphics.renderComponentHoverEffect(font, message.styleAtMouseX(i), i, j)
    }

    private fun Component.styleAtMouseX(mouseX: Int) =
        font.splitter.componentStyleAtWidth(this, mouseX - x)
}
