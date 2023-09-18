package com.noxcrew.sheeplib.widget.text

import net.minecraft.client.gui.Font
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.StringWidget
import net.minecraft.network.chat.Component
import net.minecraft.util.CommonColors

public class CenteredTextWidget(component: Component, font: Font, width: Int) :
    StringWidget(width, font.lineHeight, component, font) {
    override fun renderWidget(guiGraphics: GuiGraphics, i: Int, j: Int, f: Float) {
        guiGraphics.drawCenteredString(font, message, x + width / 2, y, CommonColors.WHITE)
    }
}
