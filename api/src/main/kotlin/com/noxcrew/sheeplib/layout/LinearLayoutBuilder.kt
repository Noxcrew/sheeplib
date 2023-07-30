package com.noxcrew.sheeplib.layout

import com.noxcrew.sheeplib.dialog.Dialog
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
 * Applies a new [LinearLayoutBuilder] to this dialog.
 */
public inline fun Dialog.linear(
    width: Int,
    height: Int,
    orientation: Orientation,
    builder: LinearLayoutBuilder.() -> Unit
): LinearLayout =

    LinearLayoutBuilder(
        x + theme.dimensions.paddingOuter,
        y + theme.dimensions.paddingOuter,
        width,
        height,
        orientation,
        theme.dimensions.paddingInner
    )
        .also(builder)
        .build()
