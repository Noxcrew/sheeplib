package com.noxcrew.sheeplib.dialog

import com.mojang.blaze3d.systems.RenderSystem
import com.noxcrew.sheeplib.CompoundWidget
import com.noxcrew.sheeplib.DialogContainer
import com.noxcrew.sheeplib.dialog.title.DialogTitleWidget
import com.noxcrew.sheeplib.theme.Theme
import com.noxcrew.sheeplib.theme.Themed
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.AbstractWidget
import net.minecraft.client.gui.layouts.Layout
import net.minecraft.client.renderer.RenderType
import net.minecraft.util.Mth
import org.jetbrains.annotations.ApiStatus.ScheduledForRemoval
import java.io.Closeable

/**
 * A base class for an interactable element present on the HUD.
 */
public abstract class Dialog(
    x: Int,
    y: Int,
) : CompoundWidget(x, y, 0, 0), Themed, Closeable {

    /** The dialog's parent. */
    public var parent: Dialog? = null
        private set

    private var dragStartX = -1
    private var dragStartY = -1

    /** The dialog's state. */
    public var state: State = State.READY
        private set

    @get:Deprecated("Check state directly", ReplaceWith("state.isClosing"))
    @get:ScheduledForRemoval(inVersion = "1.0.0")
    public val isClosing: Boolean get() = state.isClosing

    /**
     * A child dialog to show over the top of this one.
     */
    public var popup: Dialog? = null
        private set(value) {
            field = value
            children().forEach { (it as? AbstractWidget)?.active = value == null }
        }

    /**
     * Opens a dialog as a popup.
     *
     * @see [popup]
     */
    public fun popup(dialog: Dialog, replace: Boolean = false) {
        dialog.initIfNeeded()
        parent.let { p ->
            if (replace && p == null) {
                DialogContainer += dialog
                close()
                return
            }
            (p?.takeIf { replace } ?: this).let {
                dialog.parent = it
                it.popup = dialog
                it.focused = dialog
            }
        }
    }

    /**
     * Gets whether this dialog has a popup open.
     */
    public fun isPopupFocused(): Boolean = popup != null

    /**
     * Closes this dialog.
     */
    public final override fun close() {
        if (state.isClosing) return
        state = State.CLOSING
        parent?.also {
            if (it.popup == this) it.popup = null
            if (it.focused == this) it.focused = null
        } ?: run { DialogContainer -= this }
        onClose()
        state = State.CLOSED
    }

    /** This method is called before the dialog is closed. By default, it does nothing. */
    protected open fun onClose() {

    }

    final override fun setX(i: Int) {
        val adjustedX = Mth.clamp(i, 0, (Minecraft.getInstance().window.guiScaledWidth) - width)
        dialogLayout.x = adjustedX + theme.dimensions.paddingOuter
        title?.x = adjustedX
        super.setX(adjustedX)
    }

    final override fun setY(i: Int) {
        val adjustedY =
            Mth.clamp(i, 0, (Minecraft.getInstance().window.guiScaledHeight) - height - BOTTOM_EDGE_DEAD_ZONE)
        dialogLayout.y = adjustedY + theme.dimensions.paddingOuter + titleHeight()
        title?.y = adjustedY
        super.setY(adjustedY)
    }

    // fixme: why is this different to Dialog.layout?
    private lateinit var dialogLayout: Layout

    protected abstract fun layout(): Layout

    protected open val title: DialogTitleWidget? = null

    private fun titleHeight() = title?.height ?: 0

    /**
     * Initialises the dialog, building its layout and setting its dimensions.
     * You don't need to call this method when setting up, the [DialogContainer] will do that.
     * You can call this method at any time to rebuild the dialog.
     */
    protected fun init() {
        dialogLayout = layout()
        dialogLayout.arrangeElements()
        children = buildList {
            dialogLayout.visitWidgets(::add)
        }
        width = dialogLayout.width + theme.dimensions.paddingOuter * 2
        height = titleHeight() + dialogLayout.height + theme.dimensions.paddingOuter * 2

        title?.let(::addChild)

        // re-set x and y to set constraints
        y = y
        x = x
        children().forEach {
            (it as? DialogTitleWidget)?.onDialogResize()
        }

        state = State.ACTIVE
    }

    /** Initialises the dialog if it hasn't been already. */
    public fun initIfNeeded() {
        if (state == State.READY) init()
    }

    override fun mouseClicked(d: Double, e: Double, i: Int): Boolean {
        if ((popup?.mouseClicked(d, e, i) == true) || super.mouseClicked(d, e, i)) return true
        if (!isMouseOver(d, e)) return false
        isDragging = true
        dragStartX = x - d.toInt()
        dragStartY = y - e.toInt()
        return true
    }

    override fun mouseReleased(d: Double, e: Double, i: Int): Boolean {
        dragStartX = -1
        return super.mouseReleased(d, e, i)
    }

    override fun mouseDragged(d: Double, e: Double, i: Int, f: Double, g: Double): Boolean {
        if ((popup?.mouseDragged(d, e, i, f, g) == true) || super.mouseDragged(d, e, i, f, g)) return true
        if (!isDragging || dragStartX == -1) return false
        x = dragStartX + d.toInt()
        y = dragStartY + e.toInt()
        return true
    }

    /**
     * Renders the widget's background.
     * The default implementation renders an opaque [Theme.Colors.dialogBackground] background with a
     * [Theme.Colors.border] border.
     */
    protected open fun renderBackground(graphics: GuiGraphics) {
        graphics.fill(
            RenderType.gui(),
            x,
            y,
            x + getWidth(),
            y + getHeight(),
            if (parent == null) theme.colors.dialogBackgroundAlt else theme.colors.dialogBackground
        )
        if (theme.dialogBorders) graphics.renderOutline(x, y, getWidth(), getHeight(), theme.colors.border)
    }

    override fun renderWidget(graphics: GuiGraphics, i: Int, j: Int, f: Float) {
        if (isPopupFocused()) {
            RenderSystem.setShaderColor(1f, 1f, 1f, POPUP_FOCUSED_OPACITY)
        }
        renderBackground(graphics)
        super.renderWidget(graphics, i, j, f)
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f)
        popup?.let {
            graphics.pose().pushPose()
            graphics.pose().translate(0f, 0f, 1f)
            it.render(graphics, i, j, f)
            graphics.pose().popPose()
        }
    }

    protected companion object {
        public const val BOTTOM_EDGE_DEAD_ZONE: Int = 20
        private const val POPUP_FOCUSED_OPACITY = 0.7f
    }

    /** A dialog's state. */
    public enum class State(public val isClosing: Boolean) {
        /** The dialog has been created but not yet initialised. */
        READY(false),

        /** The dialog has been initialised and is ready to be used. */
        ACTIVE(false),

        /** The dialog is being closed. */
        CLOSING(true),

        /** The dialog has been closed. */
        CLOSED(true),
        ;
    }
}
