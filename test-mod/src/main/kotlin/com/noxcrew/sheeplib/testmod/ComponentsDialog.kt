package com.noxcrew.sheeplib.testmod

import com.noxcrew.sheeplib.dialog.Dialog
import com.noxcrew.sheeplib.dialog.title.DialogTitleWidget
import com.noxcrew.sheeplib.dialog.title.TextTitleWidget
import com.noxcrew.sheeplib.layout.grid
import com.noxcrew.sheeplib.theme.Theme
import com.noxcrew.sheeplib.theme.Themed
import com.noxcrew.sheeplib.widget.DropdownButton
import com.noxcrew.sheeplib.widget.SliderWidget
import com.noxcrew.sheeplib.widget.ThemedButton
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.components.StringWidget
import net.minecraft.client.gui.layouts.GridLayout
import net.minecraft.network.chat.Component

/** A dialog showcasing available components. */
public class ComponentsDialog(x: Int, y: Int) : Dialog(x, y), Themed by Theme.Active {

    override val title: DialogTitleWidget = TextTitleWidget(this, Component.literal("Components"))
    override fun layout(): GridLayout = grid {
        val font = Minecraft.getInstance().font

        StringWidget(Component.literal("Buttons"), font).at(0, 0)
        ThemedButton(Component.literal("Default")) {}.at(1, 0)
        ThemedButton(
            Component.literal("Positive"),
            style = theme.buttonStyles.positive,
        ) {}.at(2, 0)
        ThemedButton(
            Component.literal("Negative"),
            style = theme.buttonStyles.negative,
        ) {}.at(3, 0)

        DropdownButton(
            this@ComponentsDialog,
            (0..10).map { "Dropdown $it" },
            displayMapper = Component::literal
        ).at(4, 0)

        StringWidget(Component.literal("Buttons"), font).at(0, 0)

        SliderWidget(100, 0, 8, this@ComponentsDialog).atBottom(0)
    }
}
