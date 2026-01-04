package com.noxcrew.sheeplib.testmod

import com.noxcrew.sheeplib.dialog.Dialog
import com.noxcrew.sheeplib.dialog.title.DialogTitleWidget
import com.noxcrew.sheeplib.dialog.title.TextTitleWidget
import com.noxcrew.sheeplib.layout.linear
import com.noxcrew.sheeplib.theme.Theme
import com.noxcrew.sheeplib.theme.Themed
import com.noxcrew.sheeplib.widget.DropdownButton
import com.noxcrew.sheeplib.widget.SliderWidget
import com.noxcrew.sheeplib.widget.TextWidgets
import com.noxcrew.sheeplib.widget.ThemedButton
import com.noxcrew.sheeplib.widget.ToggleButton
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.layouts.Layout
import net.minecraft.client.gui.layouts.LinearLayout
import net.minecraft.network.chat.Component

/** A dialog showcasing available components. */
public class ComponentsDialog(x: Int, y: Int) : Dialog(x, y), Themed by Theme.Active {

    private enum class TestEnum {
        One,
        Two,
        Three,
        Four,
        Five,
    }

    override val title: DialogTitleWidget = TextTitleWidget(this, Component.literal("Components"))
    override fun layout(): Layout = linear(LinearLayout.Orientation.VERTICAL) {
        defaultAlignment(0.5f)
        val font = Minecraft.getInstance().font

        +TextWidgets.singleLine(Component.literal("Buttons"), font)
        +ThemedButton(Component.literal("Default")) {}
        +ThemedButton(
            Component.literal("Positive"),
            style = theme.buttonStyles.positive,
        ) {}
        +ThemedButton(
            Component.literal("Negative"),
            style = theme.buttonStyles.negative,
        ) {}

        +ThemedButton(
            Component.literal("Really long button text that should scroll"),
        ) {}

        +DropdownButton(
            this@ComponentsDialog,
            (0..100).map { "Dropdown $it" },
            displayMapper = Component::literal
        )

        +ToggleButton.enum<TestEnum>("") {}

        +SliderWidget(100, 0, 8, this@ComponentsDialog, initial = -1)
    }
}
