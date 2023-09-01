package com.noxcrew.sheeplib.widget.progress

import com.noxcrew.sheeplib.theme.Themed
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.AbstractWidget
import net.minecraft.client.gui.narration.NarrationElementOutput
import net.minecraft.network.chat.Component
import org.jetbrains.annotations.ApiStatus
import kotlin.math.roundToInt

/**
 * A progress bar.
 * The bar consists of a solid background spanning the entire size of the widget,
 * and a solid progress indicator on top of it spanning horizontally from the left edge.
 * */
@ApiStatus.Experimental
public class ProgressBar(width: Int, height: Int, theme: Themed, initialProgress: Double = 0.0) :
    AbstractWidget(0, 0, width, height, Component.empty()), Themed by theme {

    /**
     * The widget's progress, between 0.0 and 1.0.
     * Values will be clamped to this range when set.
     */
    public var progress: Double = initialProgress
        set(value) {
            field = value.coerceIn(0.0, 1.0)
        }

    /**
     * Whether the progress bar is paused. If true, progress will not be displayed.
     */
    public var paused: Boolean = false
    override fun renderWidget(guiGraphics: GuiGraphics, i: Int, j: Int, f: Float) {
        guiGraphics.fill(x, y, x + width, y + height, theme.colors.widgetBackgroundSecondary)
        guiGraphics.fill(x, y, x + (width * progress).roundToInt(), y + height, theme.colors.textPrimary)
    }

    override fun updateWidgetNarration(narrationElementOutput: NarrationElementOutput): Unit = Unit
}
