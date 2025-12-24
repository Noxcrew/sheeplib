package com.noxcrew.sheeplib.dialog

import com.noxcrew.sheeplib.AbstractWidgetExt
import com.noxcrew.sheeplib.CompoundWidget
import com.noxcrew.sheeplib.DialogContainer
import com.noxcrew.sheeplib.GuiGraphicsExt
import com.noxcrew.sheeplib.dialog.title.DialogTitleWidget
import com.noxcrew.sheeplib.theme.Theme
import com.noxcrew.sheeplib.theme.Themed
import net.minecraft.ChatFormatting
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.AbstractWidget
import net.minecraft.client.gui.layouts.Layout
import net.minecraft.client.input.MouseButtonEvent
import net.minecraft.client.renderer.RenderPipelines
import net.minecraft.network.chat.Component
import net.minecraft.util.ARGB
import net.minecraft.util.Mth
import org.jetbrains.annotations.ApiStatus.ScheduledForRemoval
import org.slf4j.LoggerFactory
import java.io.Closeable
import kotlin.reflect.jvm.jvmName

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
        try {
            dialog.initIfNeeded()
        } catch (ex: Throwable) {
            Minecraft.getInstance().gui.chat.addMessage(
                Component.translatable("sheeplib.error").withStyle { it.withColor(ChatFormatting.RED) }
            )
            LoggerFactory.getLogger("SheepLib").error("Exception while initialising ${dialog::class.jvmName}:\n" + ex.stackTraceToString())
            return
        }

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

    override fun mouseClicked(mouseButtonEvent: MouseButtonEvent, bl: Boolean): Boolean {
        if ((popup?.mouseClicked(mouseButtonEvent, bl) == true) || super.mouseClicked(mouseButtonEvent, bl)) return true
        if (!isMouseOver(mouseButtonEvent.x, mouseButtonEvent.y)) return false
        isDragging = true
        dragStartX = x - mouseButtonEvent.x.toInt()
        dragStartY = y - mouseButtonEvent.y.toInt()
        return true
    }

    override fun mouseReleased(mouseButtonEvent: MouseButtonEvent): Boolean {
        popup?.mouseReleased(mouseButtonEvent)
        dragStartX = -1
        return super.mouseReleased(mouseButtonEvent)
    }

    override fun mouseDragged(mouseButtonEvent: MouseButtonEvent, d: Double, e: Double): Boolean {
        if ((popup?.mouseDragged(mouseButtonEvent, d, e) == true) || super.mouseDragged(mouseButtonEvent, d, e)) return true
        if (!isDragging || dragStartX == -1) return false
        x = dragStartX + mouseButtonEvent.x.toInt()
        y = dragStartY + mouseButtonEvent.y.toInt()
        return true
    }

    override fun mouseScrolled(d: Double, e: Double, f: Double, g: Double): Boolean {
        if (popup?.mouseScrolled(d, e, f, g) == true) return true
        return super.mouseScrolled(d, e, f, g)
    }

    /**
     * Renders the widget's background.
     * The default implementation renders an opaque [Theme.Colors.dialogBackground] background with a
     * [Theme.Colors.border] border.
     */
    protected open fun renderBackground(graphics: GuiGraphics) {
        val baseColor = if (parent == null) theme.colors.dialogBackgroundAlt else theme.colors.dialogBackground
        graphics.fill(
            RenderPipelines.GUI,
            x,
            y,
            x + getWidth(),
            y + getHeight(),
            if (isPopupFocused()) ARGB.color(ARGB.alpha(baseColor) * POPUP_FOCUSED_OPACITY, baseColor) else baseColor
        )

        // graphics.submitOutline(...) is deferred and causes the outline to be drawn on the wrong z layer
        if (theme.dialogBorders) {
            val color = theme.colors.border
            graphics.fill(x, y, x + getWidth(), y + 1, color)
            graphics.fill(x, y + getHeight() - 1, x + getWidth(), y + getHeight(), color)
            graphics.fill(x, y + 1, x + 1, y + getHeight() - 1, color)
            graphics.fill(
                x + getWidth() - 1,
                y + 1,
                x + getWidth(),
                y + getHeight() - 1,
                color
            )
        }
    }

    override fun renderWidget(graphics: GuiGraphics, i: Int, j: Int, f: Float) {
        renderBackground(graphics)

        // Ensure none of the elements in the child widget can be focused
        val focused = isPopupFocused()
        for (it in children) {
            if (it is AbstractWidgetExt) {
                it.`sheeplib$setHoverable`(!focused)
            }
        }

        if (focused) {
            // If the pop-up is focused we render the sub-dialog with an opacity override so all text
            // is drawn slightly opaque. We cannot draw everything with a reduced opacity but text is
            // most common so worth overriding.
            val override = (graphics as GuiGraphicsExt).`sheeplib$getTextOpacityOverride`()
            (graphics as GuiGraphicsExt).`sheeplib$setTextOpacityOverride`(POPUP_FOCUSED_OPACITY)
            super.renderWidget(graphics, i, j, f)
            (graphics as GuiGraphicsExt).`sheeplib$setTextOpacityOverride`(override)
        } else {
            super.renderWidget(graphics, i, j, f)
        }

        popup?.render(graphics, i, j, f)
    }

    internal companion object {
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
