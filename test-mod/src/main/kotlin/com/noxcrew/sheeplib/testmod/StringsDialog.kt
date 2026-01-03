package com.noxcrew.sheeplib.testmod

import com.noxcrew.sheeplib.dialog.Dialog
import com.noxcrew.sheeplib.dialog.title.DialogTitleWidget
import com.noxcrew.sheeplib.dialog.title.TextTitleWidget
import com.noxcrew.sheeplib.layout.LinearLayout
import com.noxcrew.sheeplib.layout.linear
import com.noxcrew.sheeplib.theme.Theme
import com.noxcrew.sheeplib.theme.Themed
import com.noxcrew.sheeplib.util.ComponentBuilder
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.components.MultiLineTextWidget
import net.minecraft.client.gui.components.StringWidget
import net.minecraft.client.gui.layouts.Layout
import net.minecraft.client.gui.layouts.LinearLayout
import net.minecraft.network.chat.Component.literal
import net.minecraft.network.chat.HoverEvent
import net.minecraft.network.chat.TextColor

public class StringsDialog(x: Int, y: Int) : Dialog(x, y), Themed by Theme.Active {

    override val title: DialogTitleWidget = TextTitleWidget(this, literal("Strings"))

    override fun layout(): Layout = linear(LinearLayout.Orientation.VERTICAL) {
        val font = Minecraft.getInstance().font

        +StringWidget(literal("Simple string"), font).setMaxWidth(100)
        +StringWidget(literal("CLAMPED Really long string that is too big to fit in the button"), font).setMaxWidth(100)
        +StringWidget(literal("SCROLL Really long string that is too big to fit in the button"), font).setMaxWidth(
            100,
            StringWidget.TextOverflow.SCROLLING
        )

        +MultiLineTextWidget(literal("Multi line 1\nMulti line 2\nMulti line 3"), font).setMaxWidth(100)
        +MultiLineTextWidget(
            literal("Multi line 1\nMulti line 2 but really long, too big to fit\nMulti line 3"),
            font
        ).setMaxWidth(100)

        +StringWidget(
            ComponentBuilder.build {
                +"literal"
                space()
                +ComponentBuilder.literal("hover") {
                    hoverEvent = HoverEvent.ShowText(literal("hover text"))
                    underline = true
                    color = TextColor.fromRgb(0xff0000)
                }
                space()
                +"literal"
            },
            font
        )
    }
}
