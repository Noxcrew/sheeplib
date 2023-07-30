package com.noxcrew.sheeplib.dialog

import com.mojang.blaze3d.systems.RenderSystem
import com.noxcrew.sheeplib.CompoundWidget
import com.noxcrew.sheeplib.DialogContainer
import com.noxcrew.sheeplib.theme.Theme
import com.noxcrew.sheeplib.theme.Themed
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.AbstractWidget
import net.minecraft.client.gui.layouts.Layout
import net.minecraft.client.renderer.RenderType
import net.minecraft.util.Mth

/**
 * A base class for an interactable element present on the HUD.
 */
public abstract class Dialog(
    x: Int,
    y: Int
) : CompoundWidget(x, y, 0, 0), Themed {

    public var parent: Dialog? = null
        private set

    private var dragStartX = -1
    private var dragStartY = -1

    public var isClosing: Boolean = false
        protected set

    /**
     * A child dialog to show over the top of this one.
     */
    public var popup: Dialog? = null
        set(value) {
            field = value
            children().forEach { (it as? AbstractWidget)?.active = value == null }
        }

    /**
     * Opens a dialog as a popup.
     *
     * @see [popup]
     */
    public fun popup(dialog: Dialog, replace: Boolean = false) {
        parent.let { p ->
            if (replace && p == null) {
                DialogContainer += dialog
                close()
                return
            }
            (if (p != null && replace) p else this).let {
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
    public open fun close() {
        isClosing = true
        parent?.also {
            if (it.popup == this) it.popup = null
        } ?: run { DialogContainer -= this }
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
     * Sets this dialog up. Must be called by the child class due to constructor ordering.
     */
    protected fun init() {
        dialogLayout = layout()
        dialogLayout.arrangeElements()
        children = buildList {
            dialogLayout.visitWidgets(::add)
        }
        width = dialogLayout.width + theme.dimensions.paddingOuter * 2
        height = titleHeight() + dialogLayout.height + theme.dimensions.paddingOuter * 2

        title?.let { addChild(title!!) }

        // re-set x and y to set constraints
        y = y
        x = x
        children().forEach {
            (it as? DialogTitleWidget)?.onDialogResize()
        }
    }

    override fun mouseClicked(d: Double, e: Double, i: Int): Boolean {
        if ((popup?.mouseClicked(d, e, i) == true) || super.mouseClicked(d, e, i)) return true
        if (!clicked(d, e)) return false
        isDragging = false
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
        // yes, isDragging is backwards - it's a vanilla feature that
        // behaves in the exact opposite way to what i need
        if (isDragging || dragStartX == -1) return false
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

    override fun render(graphics: GuiGraphics, i: Int, j: Int, f: Float) {
        if (isPopupFocused()) {
            RenderSystem.setShaderColor(1f, 1f, 1f, POPUP_FOCUSED_OPACITY)
        }
        renderBackground(graphics)
        super.render(graphics, i, j, f)
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f)
        popup?.let {
            graphics.pose().pushPose()
            graphics.pose().translate(0f, 0f, 1f)
            it.render(graphics, i, j, f)
            graphics.pose().popPose()
        }
    }

    protected companion object {
        public const val DEFAULT_OUTER_PADDING: Int = 5

        public const val BOTTOM_EDGE_DEAD_ZONE: Int = 20
        private const val POPUP_FOCUSED_OPACITY = 0.7f
    }

}