package com.noxcrew.sheeplib

import com.noxcrew.sheeplib.dialog.Dialog
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.Renderable
import net.minecraft.client.gui.components.events.ContainerEventHandler
import net.minecraft.client.gui.components.events.GuiEventListener
import net.minecraft.client.gui.narration.NarratableEntry
import net.minecraft.client.gui.narration.NarrationElementOutput
import net.minecraft.client.gui.screens.ChatScreen

/**
 * The container for all open dialogs.
 */
public object DialogContainer : Renderable, ContainerEventHandler, NarratableEntry {

    private val minecraft = Minecraft.getInstance()

    /** The container's children. */
    private var children: List<GuiEventListener> = listOf()

    /** The container's focused child, if any. */
    private var focused: Dialog? = null

    /** Whether the container is currently being dragged. */
    private var isDragging: Boolean = false

    /** The step to offset each dialog by in the Z-axis. */
    private const val Z_OFFSET: Float = 5f

    /** Renders widgets. */
    override fun render(guiGraphics: GuiGraphics, i: Int, j: Int, f: Float) {
        val cursorIsActive = minecraft?.screen is ChatScreen

        val childX = if (cursorIsActive) i else -1
        val childY = if (cursorIsActive) j else -1

        guiGraphics.pose().pushPose()
        guiGraphics.pose().translate(0f, 0f, -children.size * Z_OFFSET)
        children.forEach {
            guiGraphics.pose().translate(0f, 0f, Z_OFFSET)
            (it as Renderable).render(guiGraphics, childX, childY, f)
        }
        guiGraphics.pose().popPose()
    }

    /** Returns an immutable view of this container's dialog. */
    override fun children(): List<GuiEventListener> = children

    /** Adds a dialog to the container and focuses it. */
    public operator fun <T> plusAssign(dialog: T) where T: GuiEventListener, T: Renderable {
        children += dialog
        if (dialog is Dialog) {
            dialog.initIfNeeded()
            focused = dialog
        }
    }

    /** Removes a dialog from the container. */
    public operator fun <T> minusAssign(dialog: T) where T: GuiEventListener, T: Renderable {
        children -= dialog
        if (focused == dialog) focused = null
    }

    /** Moves a dialog above all other dialogs. */
    public fun moveToTop(dialog: Dialog) {
        if (dialog.state.isClosing) return
        // fixme: potential thread safety issue
        children = (children - dialog + dialog)
    }

    /** Returns the focused dialog. */
    override fun getFocused(): Dialog? = focused

    /**
     * Sets the focused dialog.
     * @throws IllegalArgumentException if [dialog] is both not null and not in this container
     */
    override fun setFocused(dialog: GuiEventListener?) {
        if (dialog !is Dialog) return
        require(dialog in children) {
            "New focused element is not in the container"
        }
        focused = dialog
    }

    /**
     * Handle mouse click in reverse order.
     * This is to match rendering order, children rendered last (at the top of the screen) handle clicks first.
     */
    override fun mouseClicked(d: Double, e: Double, i: Int): Boolean {
        val child = children().lastOrNull {
            it.mouseClicked(d, e, i)
        } ?: return false

        if (focused != child && child is Dialog && !child.state.isClosing) {
            setFocused(child)
            moveToTop(child)
        }

        if (i == 0) {
            isDragging = true
        }
        return true
    }

    override fun mouseDragged(d: Double, e: Double, i: Int, f: Double, g: Double): Boolean
    = children.lastOrNull { it.mouseDragged(d,e, i, f, g) } != null

    override fun isDragging(): Boolean = isDragging

    override fun setDragging(bl: Boolean) {
        isDragging = bl
    }

    /** Unused. */
    override fun updateNarration(narrationElementOutput: NarrationElementOutput): Unit = Unit

    /** Unused. */
    override fun narrationPriority(): NarratableEntry.NarrationPriority = NarratableEntry.NarrationPriority.NONE
}
