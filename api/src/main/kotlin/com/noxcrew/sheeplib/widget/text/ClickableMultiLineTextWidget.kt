package com.noxcrew.sheeplib.widget.text

import com.noxcrew.sheeplib.theme.Theme
import com.noxcrew.sheeplib.theme.Themed
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Font
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.AbstractStringWidget
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.Style
import net.minecraft.util.FormattedCharSequence
import kotlin.math.max

/** Like [net.minecraft.client.gui.components.StringWidget] but multi-line, and clickable. */
public class ClickableMultiLineTextWidget(
    component: Component,
    font: Font,
    theme: Themed = Theme.Active,
    private val maxLines: Int = -1,
    private val maxWidth: Int = -1,
) :
    AbstractStringWidget(
        0, 0, maxWidth, 0,
        component, font
    ), Themed by theme {

    private lateinit var lines: List<FormattedCharSequence>

    override fun setMessage(component: Component) {
        lines = font.split(component, if (maxWidth > 0) maxWidth else Int.MAX_VALUE).let {
            if (maxLines > 0) it.take(maxLines) else it
        }
        height = lines.size * font.lineHeight
        width = max(maxWidth, lines.maxOf(font::width))
    }

    init {
        message = message
    }

    private fun styleAt(mouseX: Int, mouseY: Int): Style? =
        lines.getOrNull((mouseY - y) / font.lineHeight)?.styleAtMouseX(mouseX)


    private fun FormattedCharSequence.styleAtMouseX(mouseX: Int) =
        font.splitter.componentStyleAtWidth(this, mouseX - x)

    override fun renderWidget(graphics: GuiGraphics, mouseX: Int, mouseY: Int, f: Float) {
        var height = y
        var hoverStyle: Style? = null
        val shouldHover = isActive && isMouseOver(mouseX.toDouble(), mouseY.toDouble())
        lines.forEachIndexed { idx, text ->
            graphics.drawString(font, text, x, height, theme.colors.textPrimary)
            if (shouldHover && mouseY > height && height + font.lineHeight >= mouseY) hoverStyle =
                text.styleAtMouseX(idx)
            height += font.lineHeight
        }
        hoverStyle?.let {
            graphics.renderComponentHoverEffect(Minecraft.getInstance().font, it, mouseX, mouseY)
        }
    }

    override fun onClick(mouseX: Double, mouseY: Double) {
        Minecraft.getInstance().screen?.handleComponentClicked(styleAt(mouseX.toInt(), mouseY.toInt()))
    }
}
