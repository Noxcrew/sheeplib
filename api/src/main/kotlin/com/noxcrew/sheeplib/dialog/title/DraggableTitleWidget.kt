package com.noxcrew.sheeplib.dialog.title

import com.noxcrew.sheeplib.dialog.Dialog
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.network.chat.Component

// fixme: this almost certainly does not work properly
public open class DraggableTitleWidget(
    dialog: Dialog,
    component: Component,
    private val hasCloseButton: Boolean = true,
    heightFactor: Double = 1.5
) :
    TitleWidget(
        dialog,
        Minecraft.getInstance().font.width(component) + CLOSE_WIDTH,
        (Minecraft.getInstance().font.lineHeight * heightFactor).toInt(),
        component
    ) {

    private val minecraft = Minecraft.getInstance()
    private val textYOffset = (height - minecraft.font.lineHeight - 1) / 2

    private fun titleStyleAt(mouseX: Int) =
        Minecraft.getInstance().font.splitter.componentStyleAtWidth(
            message,
            mouseX - x
        )

    override fun renderWidget(graphics: GuiGraphics, i: Int, j: Int, f: Float) {
        graphics.drawString(
            minecraft.font,
            message,
            x,
            y + textYOffset,
            dialog.theme.colors.textPrimary
        )
        graphics.hLine(
            dialog.x,
            dialog.x + dialog.width - 1,
            y + height,
            dialog.theme.colors.border
        )
        if (hasCloseButton) {
            graphics.vLine(
                dialog.x + dialog.width - CLOSE_WIDTH,
                dialog.y,
                y + height,
                dialog.theme.colors.border
            )
            graphics.drawCenteredString(
                minecraft.font,
                Component.literal("x"),
                dialog.x + dialog.width - (CLOSE_WIDTH / 2),
                y + textYOffset,
                dialog.theme.colors.textPrimary
            )
        }

        if (!this.isMouseOver(i.toDouble(), j.toDouble())) return
        val style = titleStyleAt(i)
        graphics.renderComponentHoverEffect(minecraft.font, style, i, j)
    }

    override fun mouseClicked(d: Double, e: Double, i: Int): Boolean = when {
        !this.isMouseOver(d, e) -> super.mouseClicked(d, e, i)
        hasCloseButton && d >= dialog.x + dialog.width - CLOSE_WIDTH -> {
            dialog.close()
            true
        }

        else -> {
            val style = titleStyleAt(d.toInt())
            if (style?.clickEvent == null) false
            else {
                minecraft.screen?.handleComponentClicked(style)
                true
            }
        }
    }

    private companion object {
        private const val CLOSE_WIDTH = 20
    }
}
