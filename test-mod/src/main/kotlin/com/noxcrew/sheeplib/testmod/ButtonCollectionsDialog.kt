package com.noxcrew.sheeplib.testmod

import com.noxcrew.sheeplib.dialog.Dialog
import com.noxcrew.sheeplib.dialog.title.DialogTitleWidget
import com.noxcrew.sheeplib.dialog.title.TextTitleWidget
import com.noxcrew.sheeplib.layout.grid
import com.noxcrew.sheeplib.layout.iconButtonRow
import com.noxcrew.sheeplib.theme.Theme
import com.noxcrew.sheeplib.theme.Themed
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.layouts.Layout
import net.minecraft.network.chat.Component

/**
 * A dialog showcasing button collection builders.
 */
public class ButtonCollectionsDialog(x: Int, y: Int) : Dialog(x, y), Themed by Theme.Active {

    private companion object {
        /** Adds a message to the chat window. */
        fun pushChatMessage(message: String) {
            Minecraft.getInstance().gui.chat.addMessage(Component.literal(message))
        }
    }

    override val title: DialogTitleWidget = TextTitleWidget(this, Component.literal("Button Collections"))

    override fun layout(): Layout = grid {

        // icon button rows display
        iconButtonRow(this@ButtonCollectionsDialog) {
            listOf("A", "B", "C", "D", "E", "F").forEach {
                it literal { pushChatMessage("Button $it pressed") }
            }
        }
    }
}
