package com.noxcrew.sheeplib.dialog.title

import com.noxcrew.sheeplib.dialog.Dialog
import net.minecraft.client.gui.components.AbstractWidget
import net.minecraft.client.gui.narration.NarrationElementOutput
import net.minecraft.client.gui.navigation.ScreenRectangle
import net.minecraft.network.chat.Component

public abstract class AbstractTitleWidget(override val dialog: Dialog, height: Int, component: Component) :
    DialogTitleWidget, AbstractWidget(0, 0, 0, height, component) {
    override fun getWidth(): Int = dialog.width - (dialog.theme.dimensions.paddingOuter * 2)
    override fun getRectangle(): ScreenRectangle = super<AbstractWidget>.getRectangle()
    override fun updateWidgetNarration(narrationElementOutput: NarrationElementOutput): Unit = Unit
    override fun isMouseOver(d: Double, e: Double): Boolean {
        return dialog.isMouseOver(d, e) && e < y + height
    }
}
