package com.noxcrew.sheeplib.widget

import com.noxcrew.sheeplib.util.Icon
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.AbstractWidget
import net.minecraft.client.gui.narration.NarrationElementOutput
import net.minecraft.network.chat.Component

/**
 * A button that renders an [Icon].
 *
 * @param icon the icon to render
 * @param hoverText a component to render when this button is hovered over
 * @param marginX a clickable margin either side of the icon
 * @param marginY a clickable margin above and below the icon
 * @param clickHandler a callback for when the button is clicked
 */
public class IconButton(
    private val icon: Icon,
    private val hoverText: Component? = null,
    private val marginX: Int = 0,
    private val marginY: Int = 0,
    private val clickHandler: (x: Int, y: Int) -> Unit,
) :
    AbstractWidget(0, 0, icon.width + marginX * 2, icon.height + marginY * 2, Component.empty()) {
    override fun renderWidget(guiGraphics: GuiGraphics, i: Int, j: Int, f: Float) {
        icon.blit(guiGraphics, x + marginX, y + marginY, isHovered = isHovered())
        if (isHovered() && hoverText != null) {
            guiGraphics.renderTooltip(Minecraft.getInstance().font, hoverText, x, y)
        }
    }

    override fun onClick(d: Double, e: Double): Unit = clickHandler(x, y)
    override fun updateWidgetNarration(narrationElementOutput: NarrationElementOutput): Unit = Unit
}

