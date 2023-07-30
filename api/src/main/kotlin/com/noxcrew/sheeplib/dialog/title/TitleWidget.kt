package com.noxcrew.sheeplib.dialog.title

import com.noxcrew.sheeplib.dialog.Dialog
import net.minecraft.client.gui.components.AbstractWidget
import net.minecraft.client.gui.narration.NarrationElementOutput
import net.minecraft.network.chat.Component

// fixme: what does this do? how does it relate to DialogTitleWidget?
public abstract class TitleWidget(protected val dialog: Dialog, width: Int, height: Int, component: Component) :
    AbstractWidget(0, 0, width, height, component) {
    override fun getWidth(): Int = dialog.width - (dialog.theme.dimensions.paddingOuter * 2)
    override fun updateWidgetNarration(narrationElementOutput: NarrationElementOutput): Unit = Unit
    override fun isMouseOver(d: Double, e: Double): Boolean {
        return dialog.isMouseOver(d, e) && e < y + height
    }
}
