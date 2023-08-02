package com.noxcrew.sheeplib.testmod

import com.noxcrew.sheeplib.dialog.Dialog
import com.noxcrew.sheeplib.dialog.title.DialogTitleWidget
import com.noxcrew.sheeplib.dialog.title.TextTitleWidget
import com.noxcrew.sheeplib.layout.grid
import com.noxcrew.sheeplib.theme.DefaultTheme
import com.noxcrew.sheeplib.theme.Theme
import com.noxcrew.sheeplib.theme.Themed
import com.noxcrew.sheeplib.widget.DropdownButton
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
        ThemedButton(Component.literal("Default"), theme = DefaultTheme) {}.at(1, 0)
        ThemedButton(
            Component.literal("Positive"),
            theme = DefaultTheme,
            style = DefaultTheme.buttonStyles.positive,
        ) {}.at(2, 0)
        ThemedButton(
            Component.literal("Negative"),
            theme = DefaultTheme,
            style = DefaultTheme.buttonStyles.negative,
        ) {}.at(3, 0)

        DropdownButton(
            DefaultTheme.dimensions.buttonWidth,
            DefaultTheme.dimensions.buttonHeight,
            this@ComponentsDialog,
            (0..10).map { "Dropdown $it" },
            displayMapper = Component::literal
        ).at(4, 0)

        StringWidget(Component.literal("Buttons"), font).at(0, 0)
    }
}
