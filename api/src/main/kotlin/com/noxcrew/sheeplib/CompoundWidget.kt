package com.noxcrew.sheeplib

import net.minecraft.client.gui.ComponentPath
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.AbstractWidget
import net.minecraft.client.gui.components.Renderable
import net.minecraft.client.gui.components.events.ContainerEventHandler
import net.minecraft.client.gui.components.events.GuiEventListener
import net.minecraft.client.gui.layouts.Layout
import net.minecraft.client.gui.narration.NarrationElementOutput
import net.minecraft.client.gui.navigation.FocusNavigationEvent
import net.minecraft.network.chat.Component
import kotlin.jvm.optionals.getOrNull

/**
 * A widget that acts as a container for other widgets,
 * combining the layout effects of a [Layout]
 * and the behaviour effects of a [ContainerEventHandler].
 */
public abstract class CompoundWidget(x: Int, y: Int, width: Int, height: Int) :
    AbstractWidget(x, y, width, height, Component.empty()),
    ContainerEventHandler {

    protected var children: List<GuiEventListener> = listOf()

    private var focused: GuiEventListener? = null

    /**
     * Gets an immutable view of this widget's children.
     */
    public override fun children(): List<GuiEventListener> = children

    /**
     * Adds a child to this widget. The widget will be rendered on top of others.
     */
    protected fun addChild(child: GuiEventListener) {
        children += child
    }

    /**
     * Renders the widget's background and each of its children.
     */
    override fun renderWidget(graphics: GuiGraphics, i: Int, j: Int, f: Float) {
        for (it in children) {
            if (it is Renderable) it.render(graphics, i, j, f)
        }
    }

    // --
    // Positioning
    // --

    /**
     * A layout that this widget uses. Its elements will be moved when X or Y are set.
     */
    protected open val layout: Layout? = null

    override fun setX(i: Int) {
        super.setX(i)
        layout?.x = i
    }

    override fun setY(i: Int) {
        super.setY(i)
        layout?.y = i
    }

    // --
    // Focus
    // --
    override fun setFocused(bl: Boolean) {
        if (!bl) children.forEach { it.isFocused = false }
        else if (focused == null) setFocused(children.firstOrNull())
    }


    override fun setFocused(guiEventListener: GuiEventListener?) {
        focused?.isFocused = false
        focused = guiEventListener
        focused?.isFocused = true
    }

    override fun isFocused(): Boolean = super<ContainerEventHandler>.isFocused()

    override fun getFocused(): GuiEventListener? = focused

    // --
    // Mouse
    // --
    public override fun mouseClicked(d: Double, e: Double, i: Int): Boolean = getChildAt(d, e)
        .getOrNull()
        ?.takeIf { it.mouseClicked(d, e, i) }
        ?.also { it.isFocused = true } != null

    public override fun mouseReleased(d: Double, e: Double, i: Int): Boolean =
        super<ContainerEventHandler>.mouseReleased(d, e, i)

    public override fun mouseDragged(d: Double, e: Double, i: Int, f: Double, g: Double): Boolean =
        super<ContainerEventHandler>.mouseDragged(d, e, i, f, g)


    private var isDragging: Boolean = false

    override fun isDragging(): Boolean = isDragging

    override fun setDragging(bl: Boolean) {
        isDragging = bl
    }

    override fun nextFocusPath(focusNavigationEvent: FocusNavigationEvent): ComponentPath? =
        super<ContainerEventHandler>.nextFocusPath(focusNavigationEvent)


    override fun updateWidgetNarration(narrationElementOutput: NarrationElementOutput): Unit = Unit
}
