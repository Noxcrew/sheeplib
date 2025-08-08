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

/***
 * A dialog showcasing the ability to disable dragging on specific axis
 */
public class DisabledDragAxisDialog(x: Int, y: Int) : Dialog(x, y), Themed by Theme.active {
    override val title: DialogTitleWidget = TextTitleWidget(this, Component.literal("Disabled drag axis"))

    override fun layout(): GridLayout = grid {
        val font = Minecraft.getInstance().font

        StringWidget(Component.literal("Try dragging this dialog!"), font).at(0, 0, colSpan = 2)

        ThemedButton(
            Component.literal("Toggle X"),
            theme = this@DisabledDragAxisDialog,
            style = if (disableDragX) theme.buttonStyles.negative else theme.buttonStyles.positive
        ) {
            /** Toggle the drag on X axis */
            disableDragX = !disableDragX

            /** Init to update button style */
            this@DisabledDragAxisDialog.init()
        }.at(1, 0)

        ThemedButton(
            Component.literal("Toggle Y"),
            theme = this@DisabledDragAxisDialog,
            style = if (disableDragY) theme.buttonStyles.negative else theme.buttonStyles.positive
        ) {
            /** Toggle the drag on Y axis */
            disableDragY = !disableDragY

            /** Init to update button style */
            this@DisabledDragAxisDialog.init()
        }.at(1, 1)
    }
}