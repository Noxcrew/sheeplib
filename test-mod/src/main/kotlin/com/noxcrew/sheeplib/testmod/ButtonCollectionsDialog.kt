package com.noxcrew.sheeplib.testmod

import com.noxcrew.sheeplib.dialog.Dialog
import com.noxcrew.sheeplib.dialog.title.DialogTitleWidget
import com.noxcrew.sheeplib.dialog.title.TextTitleWidget
import com.noxcrew.sheeplib.layout.grid
import com.noxcrew.sheeplib.layout.iconButtonRow
import com.noxcrew.sheeplib.theme.Theme
import com.noxcrew.sheeplib.theme.Themed
import com.noxcrew.sheeplib.util.ComponentBuilder
import com.noxcrew.sheeplib.widget.ButtonStack
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.layouts.Layout
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.TextColor

/**
 * A dialog showcasing button collection builders.
 */
public class ButtonCollectionsDialog(x: Int, y: Int) : Dialog(x, y), Themed by Theme.Active {

    private companion object {

        private val letters = listOf("A", "B", "C", "D", "E", "F")

        /** Adds a message to the chat window. */
        private fun pushChatMessage(message: String) {
            Minecraft.getInstance().gui.chat.addMessage(Component.literal(message))
        }
    }

    override val title: DialogTitleWidget = TextTitleWidget(this, Component.literal("Button Collections"))

    override fun layout(): Layout = grid {
        val buttonCount = iconButtonRow(this@ButtonCollectionsDialog) {
            letters.forEach {
                it literal { pushChatMessage("Icon button $it pressed") }
            }
        }
        ButtonStack(this@ButtonCollectionsDialog, 100, Minecraft.getInstance().font.lineHeight + 2) {
            letters.forEach {
                ComponentBuilder.literal("Button ") {
                    +ComponentBuilder.literal(it) {
                        color = TextColor.fromRgb(0x00ff00)
                    }
                } runs { pushChatMessage("Text button $it pressed") }
            }
        }.atBottom(0, buttonCount)
    }
}
