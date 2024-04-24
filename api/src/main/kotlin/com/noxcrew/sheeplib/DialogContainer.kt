package com.noxcrew.sheeplib

import com.noxcrew.sheeplib.dialog.Dialog
import kotlinx.atomicfu.atomic
import kotlinx.atomicfu.update
import net.minecraft.ChatFormatting
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.LayeredDraw
import net.minecraft.client.gui.components.Renderable
import net.minecraft.client.gui.components.events.ContainerEventHandler
import net.minecraft.client.gui.components.events.GuiEventListener
import net.minecraft.client.gui.narration.NarratableEntry
import net.minecraft.client.gui.narration.NarrationElementOutput
import net.minecraft.client.gui.screens.ChatScreen
import net.minecraft.network.chat.Component
import org.slf4j.LoggerFactory
import kotlin.reflect.jvm.jvmName

/**
 * The container for all open dialogs.
 */
public object DialogContainer : LayeredDraw.Layer, ContainerEventHandler, NarratableEntry {

    private val minecraft = Minecraft.getInstance()
    private val logger = LoggerFactory.getLogger("SheepLib")

    /** The container's children. */
    private val children = atomic(listOf<GuiEventListener>())

    /** The container's focused child, if any. */
    private var focused: Dialog? = null

    /** Whether the container is currently being dragged. */
    private var isDragging: Boolean = false

    override fun render(guiGraphics: GuiGraphics, f: Float) {
        val cursorIsActive = minecraft?.screen is ChatScreen

        val childX = if (cursorIsActive) minecraft.mouseHandler.xpos() / minecraft.window.guiScale else -1
        val childY = if (cursorIsActive) minecraft.mouseHandler.ypos() / minecraft.window.guiScale else -1

        val children = children.value

        // Share the Z space evenly between dialogs..
        val zOffsetPerDialog = LayeredDraw.Z_SEPARATION / children.size

        guiGraphics.pose().pushPose()
        children.forEach {
            (it as Renderable).render(guiGraphics, childX.toInt(), childY.toInt(), f)
            guiGraphics.pose().translate(0f, 0f, zOffsetPerDialog)
        }
        guiGraphics.pose().popPose()
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

    override fun mouseDragged(d: Double, e: Double, i: Int, f: Double, g: Double): Boolean =
        children.value.lastOrNull { it.mouseDragged(d, e, i, f, g) } != null

    override fun isDragging(): Boolean = isDragging

    override fun setDragging(bl: Boolean) {
        isDragging = bl
    }

    /** Unused. */
    override fun updateNarration(narrationElementOutput: NarrationElementOutput): Unit = Unit

    /** Unused. */
    override fun narrationPriority(): NarratableEntry.NarrationPriority = NarratableEntry.NarrationPriority.NONE

}
