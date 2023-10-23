package com.noxcrew.sheeplib.widget

import com.noxcrew.sheeplib.theme.Themed
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.AbstractWidget
import net.minecraft.client.gui.narration.NarrationElementOutput
import net.minecraft.network.chat.Component
import kotlin.math.abs
import kotlin.math.roundToInt
import kotlin.math.sign

/**
 * A slider with discrete steps between two integers.
 *
 * @param width the slider's width
 * @param min the inclusive minimum value that the slider can reach
 * @param max the inclusive maximum value that the slider can reach
 * @param themed the theme
 * @param updateCallback a callback, called when the slider is moved
 */
public class SliderWidget(
    width: Int,
    public val min: Int,
    public val max: Int,
    themed: Themed,
    private val updateCallback: ((Int) -> Unit)? = null,
) :
    AbstractWidget(0, 0, width, HEIGHT, Component.empty()),
    Themed by themed {
    private companion object {
        // fixme: don't hardcode this
        private const val HEIGHT = 14
    }

    init {
        check(max >= min + 2) { "Maximum must be larger than minimum by at least two" }
    }

    private var currentIndex: Int = 0
        set(value) {
            field = value
            updateCallback?.invoke(currentValue)
        }

    /**
     * The slider's current numeric value.
     */
    public val currentValue: Int
        get() = min + currentIndex

    // fixme: why 3 specifically?
    private val stepSize = (width - 3.0) / (max - min)

    private val offsets: List<Int> = DoubleArray(max - min + 1) { stepSize * it }.map(Double::roundToInt)

    /** Updates the slider's current position based on the mouse's absolute X co-ordinate. */
    private fun updatePosition(mouseX: Int) {
        val offsetX = mouseX - x
        currentIndex = offsets.withIndex().minBy { abs(it.value - offsetX) }.index
    }

    override fun onClick(d: Double, e: Double): Unit = updatePosition(d.toInt())
    override fun onDrag(d: Double, e: Double, f: Double, g: Double): Unit = updatePosition(d.toInt())
    override fun renderWidget(guiGraphics: GuiGraphics, i: Int, j: Int, f: Float) {
        guiGraphics.fill(x, y + (HEIGHT / 2 - 1), x + width, y + (HEIGHT / 2 + 1), theme.colors.textSecondary)

        val lineX = x + offsets[currentIndex]
        guiGraphics.fill(lineX, y, lineX + 3, y + height, theme.colors.textPrimary)
    }


    override fun mouseScrolled(d: Double, e: Double, f: Double): Boolean {
        currentIndex = (currentIndex + sign(f).toInt()).coerceIn(min..max)
        return true
    }

    override fun updateWidgetNarration(narrationElementOutput: NarrationElementOutput): Unit = Unit
}
