package com.noxcrew.sheeplib.widget

import com.noxcrew.sheeplib.mixin.ScreenAccessor
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Font
import net.minecraft.client.gui.components.AbstractStringWidget
import net.minecraft.client.gui.components.MultiLineTextWidget
import net.minecraft.client.gui.components.StringWidget
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.Style
import java.util.function.Consumer

/**
 * Utilities for creating text widgets.
 */
public object TextWidgets {

    private val font = Minecraft.getInstance().font

    private val clickHandler: Consumer<Style> = Consumer {
        val click = it.clickEvent ?: return@Consumer
        ScreenAccessor.`sheeplib$defaultHandleGameClickEvent`(
            click,
            Minecraft.getInstance(),
            null,
        )

    }

    /**
     * Applies the SheepLib click handler to a widget and sets it to active, so that it can handle click events.
     */
    public fun <T : AbstractStringWidget> T.withSheepLibClickHandler(): T = this.apply {
        setComponentClickHandler(clickHandler)
        active = true
    }

    /**
     * Creates a single-line string widget.
     */
    public fun singleLine(
        /** The text component to display. */
        text: Component,
        /** The font to use. Uses Minecraft's default font if not set. */
        font: Font = this.font,
        /** The maximum width of the widget. Defaults to [Int.MAX_VALUE]. */
        maxWidth: Int = Int.MAX_VALUE,
        /** How strings that are too long to fit should be handled. Defaults to [StringWidget.TextOverflow.CLAMPED]. */
        overflow: StringWidget.TextOverflow = StringWidget.TextOverflow.CLAMPED
    ): StringWidget = StringWidget(text, font).apply {
        withSheepLibClickHandler()
        setMaxWidth(maxWidth, overflow)
    }

    public fun multiLine(
        /** The text component to display. */
        text: Component,
        /** The font to use. Uses Minecraft's default font if not set. */
        font: Font = this.font,
        /** The widget's X coordinate. Defaults to 0. */
        x: Int = 0,
        /** The widget's Y coordinate. Defaults to 0. */
        y: Int = 0,
        /** Whether the text should be centered vertically. Defaults to false. */
        center: Boolean = false,
        /** The maximum width of the widget. */
        maxWidth: Int? = null,
        /** The maximum number of rows that the widget can show. */
        maxRows: Int? = null,
    ): MultiLineTextWidget = MultiLineTextWidget(x, y, text, font).apply {
        withSheepLibClickHandler()
        setCentered(center)
        if (maxWidth != null) setMaxWidth(maxWidth)
        if (maxRows != null) setMaxRows(maxRows)
    }
}
