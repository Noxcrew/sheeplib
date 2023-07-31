package com.noxcrew.sheeplib.dialog.title

import com.noxcrew.sheeplib.CompoundWidget
import com.noxcrew.sheeplib.StringWidgetExt
import com.noxcrew.sheeplib.dialog.Dialog
import com.noxcrew.sheeplib.layout.CanvasLayout
import com.noxcrew.sheeplib.theme.Themed
import com.noxcrew.sheeplib.widget.IconButton
import com.noxcrew.sheeplib.widget.text.ClickableTextWidget
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.navigation.ScreenRectangle
import net.minecraft.network.chat.Component

/**
 * A title widget that displays a single line of text, with an optional close button.
 *
 * @param dialog the parent dialog
 * @param component the text to show
 * @param isCloseable if true, a close button is added to the right edge of the widget
 */
public class TextTitleWidget(
    public override val dialog: Dialog,
    private val component: Component,
    private val isCloseable: Boolean = true,
) :
    CompoundWidget(0, 0, dialog.width, FONT_HEIGHT + dialog.theme.dimensions.paddingOuter * 2),
    DialogTitleWidget,
    Themed by dialog {
    private companion object {
        private const val FONT_HEIGHT = 7
    }

    override fun getWidth(): Int = layout.width
    override fun setWidth(i: Int) {
        layout.width = i
    }

    override fun getHeight(): Int = layout.height

    // fixme: replace this with something nicer
    override val layout: CanvasLayout = CanvasLayout(
        100,
        FONT_HEIGHT + theme.dimensions.paddingOuter * 2,
    ).apply {
        val font = Minecraft.getInstance().font
        ClickableTextWidget(
            component,
            font,
        ).also {
            @Suppress("CAST_NEVER_SUCCEEDS") // mixin injected
            (it as StringWidgetExt).shouldRenderWithShadow(false)
        }.at(top = theme.dimensions.paddingOuter, left = theme.dimensions.paddingOuter)
        if (isCloseable) {
            IconButton(
                theme.icons.close,
                marginY = theme.dimensions.paddingOuter + 1,
                marginX = theme.dimensions.paddingOuter,
            ) { _, _ -> dialog.close() }
                .at(top = 0, right = 0)
        }
    }

    override fun getRectangle(): ScreenRectangle = super<DialogTitleWidget>.getRectangle()

    init {
        layout.arrangeElements()
        layout.visitWidgets(this::addChild)
    }


    override fun renderWidget(graphics: GuiGraphics, i: Int, j: Int, f: Float) {
//        graphics.fill(x, y + height, x + getWidth(), y + height + 1, theme.colors.border)
        graphics.hLine(x, x + getWidth() - 1, y + height, theme.colors.border)
        super.renderWidget(graphics, i, j, f)
    }

    override fun onDialogResize() {
        setWidth(dialog.width)
        layout.arrangeElements()
    }
}

