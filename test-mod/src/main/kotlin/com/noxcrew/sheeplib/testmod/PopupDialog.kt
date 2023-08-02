package com.noxcrew.sheeplib.testmod;

import com.noxcrew.sheeplib.dialog.Dialog
import com.noxcrew.sheeplib.dialog.PromptDialog
import com.noxcrew.sheeplib.dialog.title.DialogTitleWidget
import com.noxcrew.sheeplib.dialog.title.TextTitleWidget
import com.noxcrew.sheeplib.layout.grid
import com.noxcrew.sheeplib.theme.Theme
import com.noxcrew.sheeplib.theme.Themed
import com.noxcrew.sheeplib.widget.ThemedButton
import net.minecraft.client.gui.layouts.GridLayout
import net.minecraft.network.chat.Component

/** A dialog demonstrating the popup feature. */
public class PopupDialog(x: Int, y: Int) : Dialog(x, y), Themed by Theme.Active {

    private fun createPopup() = PromptDialog(
        // the offset here makes it a bit clearer what the dialog is actually doing
        // opening at the mouse cursor can work nicely too
        x + 10, y + 10,
        theme = this,
        Component.literal("Popup"),
        Component.literal("This is a child dialog"),
    )

    override val title: DialogTitleWidget = TextTitleWidget(this, Component.literal("Popups"))

    override fun layout(): GridLayout = grid {
        ThemedButton(Component.literal("Popup")) { popup(createPopup()) }.atBottom(0)
        ThemedButton(Component.literal("Replace")) { popup(createPopup(), replace = true) }.atBottom(0)
    }
}
