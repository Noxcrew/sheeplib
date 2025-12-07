package com.noxcrew.sheeplib

import com.noxcrew.sheeplib.dialog.Dialog
import kotlinx.atomicfu.atomic
import kotlinx.atomicfu.update
import net.minecraft.ChatFormatting
import net.minecraft.client.DeltaTracker
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.Renderable
import net.minecraft.client.gui.components.events.ContainerEventHandler
import net.minecraft.client.gui.components.events.GuiEventListener
import net.minecraft.client.gui.narration.NarratableEntry
import net.minecraft.client.gui.narration.NarrationElementOutput
import net.minecraft.client.gui.screens.ChatScreen
import net.minecraft.client.input.MouseButtonEvent
import net.minecraft.network.chat.Component
import org.slf4j.LoggerFactory
import kotlin.reflect.jvm.jvmName

/**
 * The container for all open dialogs.
 */
public object DialogContainer : ContainerEventHandler, NarratableEntry, Renderable {

    private val minecraft = Minecraft.getInstance()
    private val logger = LoggerFactory.getLogger("SheepLib")

    /** The container's children. */
    private val children = atomic(listOf<GuiEventListener>())

    /** The container's focused child, if any. */
    private var focused: Dialog? = null

    /** Whether the container is currently being dragged. */
    private var isDragging: Boolean = false

    /** Renders all opened dialogs. */
    public override fun render(guiGraphics: GuiGraphics, i: Int, j: Int, f: Float) {
        val cursorIsActive = minecraft?.screen is ChatScreen

        val childX = if (cursorIsActive) minecraft.mouseHandler.getScaledXPos(minecraft.window) else -1
        val childY = if (cursorIsActive) minecraft.mouseHandler.getScaledYPos(minecraft.window) else -1
        children.value.forEach {
            (it as Renderable).render(guiGraphics, childX.toInt(), childY.toInt(), f)
        }
    }

    /** Returns an immutable view of this container's dialog. */
    override fun children(): List<GuiEventListener> = children.value

    /** Adds a dialog to the container and focuses it. */
    public operator fun <T> plusAssign(dialog: T) where T : GuiEventListener, T : Renderable {
        if (dialog is Dialog) {
            try {
                dialog.initIfNeeded()
                focused = dialog
            } catch (ex: Throwable) {
                Minecraft.getInstance().gui.chat.addMessage(
                    Component.translatable("sheeplib.error").withStyle { it.withColor(ChatFormatting.RED) }
                )
                logger.error("Exception while initialising ${dialog::class.jvmName}:\n" + ex.stackTraceToString())
                return
            }
        }
        children.update {
            it + dialog
        }
    }

    /** Removes a dialog from the container. */
    public operator fun <T> minusAssign(dialog: T) where T : GuiEventListener, T : Renderable {
        children.update { it - dialog }
        if (focused == dialog) focused = null
    }

    /** Moves a dialog above all other dialogs. */
    public fun moveToTop(dialog: Dialog) {
        if (dialog.state.isClosing) return
        children.update {
            it - dialog + dialog
        }
    }

    /** Returns the focused dialog. */
    override fun getFocused(): Dialog? = focused

    /**
     * Sets the focused dialog.
     * @throws IllegalArgumentException if [dialog] is both not null and not in this container
     */
    override fun setFocused(dialog: GuiEventListener?) {
        if (dialog == null) {
            focused?.focused = null
        }

        if (dialog !is Dialog) return
        require(dialog in children.value) {
            "New focused element is not in the container"
        }
        focused = dialog
        minecraft.screen?.focused = this
    }

    /**
     * Handle mouse click in reverse order.
     * This is to match rendering order, children rendered last (at the top of the screen) handle clicks first.
     */
    override fun mouseClicked(mouseButtonEvent: MouseButtonEvent, bl: Boolean): Boolean {
        val child = children().lastOrNull {
            it.mouseClicked(mouseButtonEvent, bl)
        } ?: return false

        if (focused != child && child is Dialog && !child.state.isClosing) {
            setFocused(child)
            moveToTop(child)
        }

        if (mouseButtonEvent.button() == 0) {
            isDragging = true
        }
        return true
    }

    override fun mouseDragged(mouseButtonEvent: MouseButtonEvent, d: Double, e: Double): Boolean =
        children.value.lastOrNull { it.mouseDragged(mouseButtonEvent, d, e) } != null

    override fun isDragging(): Boolean = isDragging

    override fun setDragging(bl: Boolean) {
        isDragging = bl
    }

    /** Unused. */
    override fun updateNarration(narrationElementOutput: NarrationElementOutput): Unit = Unit

    /** Unused. */
    override fun narrationPriority(): NarratableEntry.NarrationPriority = NarratableEntry.NarrationPriority.NONE

}
