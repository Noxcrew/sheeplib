package com.noxcrew.sheeplib.layout

import net.minecraft.client.gui.layouts.AbstractLayout
import net.minecraft.client.gui.layouts.LayoutElement
import java.util.function.Consumer

/**
 * A layout that allows for children to be arbitrarily placed, intended for manually building layouts.
 */
public class CanvasLayout(width: Int, height: Int, private val padding: Int = 0) : AbstractLayout(0, 0, width, height) {

    public fun setWidth(value: Int) {
        width = value
    }

    private data class ChildWrapper(
        val element: LayoutElement,
        val top: Int?,
        val left: Int?,
        val right: Int?,
        val bottom: Int?
    ) {
        init {
            check((top == null) xor (bottom == null)) { "Exactly one of top and bottom must be non-null" }
            check((left == null) xor (right == null)) { "Exactly one of left and right must be non-null" }
        }
    }

    private val children = mutableListOf<ChildWrapper>()

    /**
     * Adds an element to the layout.
     *
     * Only one of each axis pair (i.e. `top` and `bottom`, and `left` and `right`), may be specified.
     *
     * @param element the element
     * @param top the Y offset from the top edge
     * @param left the X offset from the left edge
     * @param right the X offset from the right edge
     * @param bottom the Y offset from the bottom edge
     */
    public fun addChild(
        element: LayoutElement,
        top: Int? = null,
        left: Int? = null,
        right: Int? = null,
        bottom: Int? = null
    ) {
        children.add(ChildWrapper(element, top, left, right, bottom))
    }

    /**
     * Adds this element to the layout.
     *
     * @see addChild
     */
    public fun LayoutElement.at(
        top: Int? = null,
        left: Int? = null,
        right: Int? = null,
        bottom: Int? = null
    ): LayoutElement = this.also { addChild(this, top, left, right, bottom) }

    override fun arrangeElements() {
        super.arrangeElements()
        children.forEach {
            it.element.x = if (it.left != null) x + padding + it.left else x + width - it.right!! - it.element.width - padding
            it.element.y = if (it.top != null) y + padding + it.top else y + height - it.bottom!! - padding
        }
    }

    override fun visitChildren(consumer: Consumer<LayoutElement>) {
        children
            .map(ChildWrapper::element)
            .forEach(consumer)
    }
}
