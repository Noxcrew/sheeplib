package com.noxcrew.sheeplib.layout

import com.noxcrew.sheeplib.theme.Themed
import net.minecraft.client.gui.layouts.LayoutElement
import net.minecraft.client.gui.layouts.LayoutSettings
import net.minecraft.client.gui.layouts.LinearLayout
import net.minecraft.client.gui.layouts.LinearLayout.Orientation

/**
 * A [LayoutBuilder] backed by a [LinearLayout].
 */
public class LinearLayoutBuilder(x: Int, y: Int, width: Int, height: Int, orientation: Orientation, padding: Int) :
    LayoutBuilder<LinearLayout> {

    private val layout = LinearLayout(x, y, width, height, orientation)

    private val _layoutSettings: LayoutSettings = LayoutSettings.defaults().apply {
        if (orientation == Orientation.HORIZONTAL) paddingVertical(padding)
        else paddingHorizontal(padding)
    }

    public val defaultLayoutSettings: LayoutSettings
        get() = _layoutSettings.copy()

    /**
     * Adds an element to the layout.
     *
     * @param layoutSettings the layout settings to use.
     * By default, [layoutSettings] is used, which includes a predefined padding.
     */
    public fun <T : LayoutElement> T.add(layoutSettings: LayoutSettings = _layoutSettings): T = this.also {
        layout.addChild(this, layoutSettings)
    }

    /**
     * Adds this element to the layout.
     *
     * @see [add]
     */
    public operator fun <T : LayoutElement> T.unaryPlus(): T = this.also { add(defaultLayoutSettings) }

    /**
     * Adds an element to the layout.
     *
     * @param settings a lambda used to configure a copy of [defaultLayoutSettings]
     */
    public inline fun <T : LayoutElement> T.add(settings: LayoutSettings.() -> Unit): T =
        this.add(defaultLayoutSettings.also(settings))

    override fun build(): LinearLayout = layout
}

/**
 * Builds a new linear layout.
 *
 * @param width the layout's width
 * @param height the layout's height
 * @param orientation the layout's orientation
 * @param padding the minimum padding between elements. Elements may be spread out further to fill available space.
 * @param builder the builder
 */
public inline fun LinearLayout(
    width: Int,
    height: Int,
    orientation: Orientation,
    padding: Int,
    builder: LinearLayoutBuilder.() -> Unit,
): LinearLayout = LinearLayoutBuilder(0, 0, width, height, orientation, padding)
    .also(builder)
    .build()

/**
 * Builds a new linear layout, using spacing from this theme.
 *
 * @param width the layout's width
 * @param height the layout's height
 * @param orientation the layout's orientation
 * @param builder the builder
 */
public inline fun Themed.linear(
    width: Int,
    height: Int,
    orientation: Orientation,
    builder: LinearLayoutBuilder.() -> Unit
): LinearLayout = LinearLayout(width, height, orientation, theme.dimensions.paddingInner, builder)
