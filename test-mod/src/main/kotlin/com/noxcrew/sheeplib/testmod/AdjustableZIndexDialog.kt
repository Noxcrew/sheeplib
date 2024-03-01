package com.noxcrew.sheeplib.testmod

import com.noxcrew.sheeplib.dialog.Dialog
import com.noxcrew.sheeplib.layout.grid
import com.noxcrew.sheeplib.theme.DefaultTheme
import com.noxcrew.sheeplib.theme.Themed
import com.noxcrew.sheeplib.widget.SliderWidget
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.layouts.Layout
import org.slf4j.LoggerFactory

/** A dialog with an adjustable Z index. */
public class AdjustableZIndexDialog(x: Int, y: Int) : Dialog(x, y), Themed by DefaultTheme {

    private var index: Int = 0
    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun renderWidget(graphics: GuiGraphics, i: Int, j: Int, f: Float) {
        graphics.pose().pushPose()
        graphics.pose().translate(0f, 0f, index.toFloat())
        super.renderWidget(graphics, i, j, f)
        graphics.pose().popPose()
    }

    override fun layout(): Layout = grid {
        SliderWidget(500, 0, 100, this@AdjustableZIndexDialog) {
            index = it
            logger.info(it.toString())
        }.atBottom(0)
    }
}
