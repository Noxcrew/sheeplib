package com.noxcrew.sheeplib.widget

import com.noxcrew.sheeplib.theme.Theme
import com.noxcrew.sheeplib.theme.Themed
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.AbstractWidget
import net.minecraft.client.gui.narration.NarrationElementOutput
import net.minecraft.network.chat.Component

/**
 * A clickable button.
 *
 * @param message The text to show on the button
 * @param clickHandler A callback for when the button is clicked
 */
public open class ThemedButton(
    width: Int,
    height: Int,
    message: Component,
    public var isEnabled: Boolean = true,
    theme: Themed = Theme.Active,
    protected open val style: Theme.ButtonStyle = theme.theme.buttonStyles.standard,
    protected val centreText: Boolean = false,
    protected val scrollText: Boolean = true,
    private val clickHandler: () -> Unit,
) :
    AbstractWidget(0, 0, width, height, message), Themed by theme {

    // fixme: why the overload?
    public constructor(
        message: Component,
        isEnabled: Boolean = true,
        theme: Themed = Theme.Active,
        style: Theme.ButtonStyle = theme.theme.buttonStyles.standard,
        centreText: Boolean = false,
        scrollText: Boolean = false,
        clickHandler: () -> Unit
    ) : this(
        theme.theme.dimensions.buttonWidth,
        theme.theme.dimensions.buttonHeight,
        message,
        isEnabled,
        theme,
        style,
        centreText,
        scrollText,
        clickHandler
    )

    private var messageWidth: Int = Minecraft.getInstance().font.width(message)

    override fun setMessage(component: Component) {
        messageWidth = Minecraft.getInstance().font.width(message)
    }

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
                renderScrollingString(
                    graphics,
                    minecraft.font,
                    message,
                    x + theme.dimensions.paddingInner,
                    getY(),
                    x + theme.dimensions.paddingInner + maxWidth,
                    getY() + getHeight(),
                    color
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
    override fun onClick(d: Double, e: Double) {
        if (isEnabled) clickHandler()
    }

    override fun updateWidgetNarration(narrationElementOutput: NarrationElementOutput) {
        this.defaultButtonNarrationText(narrationElementOutput)
    }
}
