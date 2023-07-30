package com.noxcrew.sheeplib.dialog

import com.noxcrew.sheeplib.layout.grid
import com.noxcrew.sheeplib.theme.Theme
import com.noxcrew.sheeplib.theme.Themed
import com.noxcrew.sheeplib.widget.ThemedButton
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.components.MultiLineTextWidget
import net.minecraft.client.gui.components.StringWidget
import net.minecraft.client.gui.layouts.GridLayout
import net.minecraft.network.chat.Component

/**
 * A dialog that displays text, with a single confirm button that closes the dialog.
 * @param titleText the text to display as a title
 * @param text the dialog's body text
 */
public class PromptDialog(
    x: Int, y: Int, theme: Themed = Theme.Active,
    private val titleText: Component,
    private val text: Component
) : Dialog(x, y), Themed by theme {

    public constructor(x: Int, y: Int, theme: Themed = Theme.Active, message: String) : this(
        x, y, theme,
        Component.literal(message),
        Component.literal(message)
    )

    init {
        init()
    }

    override fun layout(): GridLayout = grid {
        // fixme: use an actual title widget
        StringWidget(titleText, Minecraft.getInstance().font).at(0, 0)
        MultiLineTextWidget(text, Minecraft.getInstance().font).at(1, 0)
        ThemedButton(
            theme.dimensions.buttonWidth,
            theme.dimensions.buttonHeight,
            Component.translatable("sheeplib.dialog.ok")
        ) {
            close()
        }.at(2, 0)
    }
}
