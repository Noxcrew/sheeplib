package com.noxcrew.sheeplib.widget

import com.mojang.blaze3d.systems.RenderSystem
import com.noxcrew.sheeplib.theme.Theme
import com.noxcrew.sheeplib.theme.Themed
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.AbstractWidget
import net.minecraft.client.gui.narration.NarrationElementOutput
import net.minecraft.client.input.MouseButtonEvent
import net.minecraft.network.chat.Component

/**
 * A themed button.
 *
 * @param message The text to show on the button
 * @param width The button's width. Defaults to [Theme.Dimensions.buttonWidth]
 * @param height The button's height. Defaults to [Theme.Dimensions.buttonHeight]
 * @param isEnabled Whether the button is enabled
 * @param clickHandler A callback for when the button is clicked
 * @param style The button's style
 * @param centreText When true, text is aligned centrally, otherwise it's left-aligned.
 * @param scrollText If the text is larger than the button, scrolls it across the button.
 * This is ignored if [centreText] is true, where text will always be scrolled.
 */
public open class ThemedButton(
    message: Component,
    theme: Themed = Theme.Active,
    width: Int = theme.theme.dimensions.buttonWidth,
    height: Int = theme.theme.dimensions.buttonHeight,
    public var isEnabled: Boolean = true,
    protected open val style: Theme.ButtonStyle = theme.theme.buttonStyles.standard,
    protected val centreText: Boolean = false,
    protected val scrollText: Boolean = true,
    private val clickHandler: () -> Unit,
) :
    AbstractWidget(0, 0, width, height, message), Themed by theme {

    private var messageWidth: Int = Minecraft.getInstance().font.width(message)

    override fun setMessage(component: Component) {
        messageWidth = Minecraft.getInstance().font.width(message)
    }

    /**
     * The maximum width that the button's text can cover, excluding padding.
     * Used to calculate when to scroll text.
     */
    protected open fun maxTextWidth(): Int = width - theme.dimensions.paddingInner * 2

    override fun renderWidget(graphics: GuiGraphics, i: Int, j: Int, f: Float) {
        val minecraft = Minecraft.getInstance()
        val isHovered = isHovered()
        graphics.fill(
            x,
            y,
            x + getWidth(),
            y + getHeight(),
            when {
                !isEnabled -> style.disabledColor
                isHovered -> style.hoverColor
                else -> style.defaultColor
            }.get(theme)
        )

        val color = if (isEnabled) theme.colors.textPrimary else theme.colors.textSecondary
        val y = y + (getHeight() / 2) - (minecraft.font.lineHeight / 2)
        val maxWidth = maxTextWidth()



        when {
            messageWidth > maxWidth && scrollText -> {
                graphics.textRendererForWidget(
                    this, GuiGraphics.HoveredTextEffects.TOOLTIP_AND_CURSOR
                ).acceptScrollingWithDefaultCenter(
                    message,
                    x + theme.dimensions.paddingInner,
                    getY(),
                    x + theme.dimensions.paddingInner + maxWidth,
                    getY() + getHeight(),
//                    color
                )
            }

            centreText -> graphics.drawCenteredString(minecraft.font, message, x + 1 + width / 2, y, color)

            else -> graphics.drawString(minecraft.font, message, x + theme.dimensions.paddingInner, y, color)
        }

        if (isHovered && isActive) graphics.renderComponentHoverEffect(minecraft.font, message.style, i, j)
    }

    /**
     * Call the click callback when clicked.
     */
    override fun onClick(mouseButtonEvent: MouseButtonEvent, bl: Boolean) {
        if (isEnabled) clickHandler()
    }

    override fun updateWidgetNarration(narrationElementOutput: NarrationElementOutput) {
        this.defaultButtonNarrationText(narrationElementOutput)
    }
}
