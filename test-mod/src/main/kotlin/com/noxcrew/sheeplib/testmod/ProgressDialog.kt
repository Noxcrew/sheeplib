package com.noxcrew.sheeplib.testmod

import com.noxcrew.sheeplib.coroutines.CoroutineScopeDialog
import com.noxcrew.sheeplib.dialog.title.DialogTitleWidget
import com.noxcrew.sheeplib.dialog.title.TextTitleWidget
import com.noxcrew.sheeplib.layout.grid
import com.noxcrew.sheeplib.theme.DefaultTheme
import com.noxcrew.sheeplib.theme.Themed
import com.noxcrew.sheeplib.widget.progress.ProgressBar
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.minecraft.client.gui.layouts.Layout
import net.minecraft.network.chat.Component
import kotlin.properties.Delegates

/** A dialog for progress indicators. */
public class ProgressDialog(x: Int, y: Int) : CoroutineScopeDialog(x, y), Themed by DefaultTheme {
    override fun layout(): Layout = grid {
        ProgressBar(100, 3, this@ProgressDialog).atBottom(0)
    }

    private fun setCount(count: Int) {
        children().forEach {
            if (it is ProgressBar) it.progress = count / 100.0
        }
    }

    override val title: DialogTitleWidget = TextTitleWidget(this, Component.literal("Progress"))

    init {
        launch {
            while (true) {
                var count: Int by Delegates.observable(0) { _, _, new -> setCount(new) }
                while (count < 100) {
                    delay(25)
                    count++
                }
                while (count > 0) {
                    delay(25)
                    count--
                }
            }
        }
    }
}
