package com.noxcrew.sheeplib.testmod

import com.noxcrew.sheeplib.dialog.SimpleDialog
import com.noxcrew.sheeplib.dialog.title.TextTitleWidget
import com.noxcrew.sheeplib.layout.grid
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.components.EditBox
import net.minecraft.network.chat.Component

public fun textInputDialog(x: Int, y: Int): SimpleDialog = SimpleDialog(x, y) {
    title = TextTitleWidget(dialog, Component.literal("Text input"))
    layout = dialog.grid {
        val font = Minecraft.getInstance().font
        EditBox(font, 0, 0, 100, 12, Component.literal("hi")).atBottom(0)
    }
}
