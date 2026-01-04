package com.noxcrew.sheeplib.testmod

import com.noxcrew.sheeplib.dialog.Dialog
import com.noxcrew.sheeplib.dialog.title.TextTitleWidget
import com.noxcrew.sheeplib.layout.grid
import com.noxcrew.sheeplib.theme.Theme
import com.noxcrew.sheeplib.theme.Themed
import com.noxcrew.sheeplib.widget.TextWidgets
import com.noxcrew.sheeplib.widget.ThemedButton
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.components.Button
import net.minecraft.client.gui.layouts.GridLayout
import net.minecraft.network.chat.Component

/**
 * An example dialog. See "Creating a dialog" on the wiki.
 *
 * In the real world, you'd just use PromptDialog for something as simple as this.
 */
public class MyFirstDialog(x: Int, y: Int) : Dialog(x, y), Themed by Theme.Active {

    override val title: TextTitleWidget = TextTitleWidget(this, Component.literal("Hello!"))

    override fun layout(): GridLayout = grid {
        // Some static text.
        TextWidgets.singleLine(Component.literal("Hello world!"), Minecraft.getInstance().font).at(0, 0)

        // A close button.
        ThemedButton(
            Component.literal("Close"),
            // Dialogs implement Themed, it's good practice to use it directly to theme elements
            theme = this@MyFirstDialog
        ) {
            // Button callback - close the dialog.
            close()
        }.at(1, 0)

        Button.builder(
            Component.literal("Vanilla test")
        ) {}.build().at(2, 0)

        // GridLayoutBuilder has some nice ways of adding components
        // without having to give them all row and column values manually,
        // but that's outside the scope of this example.
        // Check GridLayoutBuilder javadocs for more info.
    }
}
