package com.noxcrew.sheeplib.testmod

import com.noxcrew.sheeplib.dialog.Dialog
import com.noxcrew.sheeplib.dialog.title.DialogTitleWidget
import com.noxcrew.sheeplib.dialog.title.TextTitleWidget
import com.noxcrew.sheeplib.layout.grid
import com.noxcrew.sheeplib.theme.Theme
import com.noxcrew.sheeplib.theme.Themed
import com.noxcrew.sheeplib.widget.ThemedButton
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.components.StringWidget
import net.minecraft.client.gui.layouts.GridLayout
import net.minecraft.network.chat.Component

/**
 * Showcases the ability to constrain dialog's movement.
 */
public class ConstrainedDialog(x: Int, y: Int) : Dialog(x, y), Themed by Theme.active {
    private var constrainY: Boolean = false

    override val title: DialogTitleWidget = TextTitleWidget(this, Component.literal("Constrained dialog"))

    override fun layout(): GridLayout = grid {
        val font = Minecraft.getInstance().font

        StringWidget(
            Component.literal("Try dragging this dialog"),
            font
        ).atBottom(0)

        ThemedButton(
            Component.literal("Toggle constrain"),
            theme = this@ConstrainedDialog
        ) {
            constrainY = !constrainY
        }.atBottom(0)
    }

    /**
     * Overrides the default dragging behavior to constrain vertical movement.
     *
     * When [constrainY] is true, the dialog's Y position remains fixed,
     * allowing only horizontal dragging.
     */
    override fun moveDragged(draggedX: Int, draggedY: Int) {
        val constrainedY = if (constrainY) y else draggedY

        /**
         * Calling super to apply default position constraints (keeping the dialog within the window bounds).
         *
         * If you want to bypass these constraints, you can directly assign values to
         * [x] and [y] instead of calling `super.moveDragged`.
         */
        super.moveDragged(draggedX, constrainedY)
    }
}