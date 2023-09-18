package com.noxcrew.sheeplib.testmod

import com.noxcrew.sheeplib.dialog.Dialog
import com.noxcrew.sheeplib.dialog.title.DialogTitleWidget
import com.noxcrew.sheeplib.layout.grid
import com.noxcrew.sheeplib.theme.DefaultTheme
import com.noxcrew.sheeplib.theme.Themed
import com.noxcrew.sheeplib.widget.ThemedButton
import com.noxcrew.sheeplib.widget.progress.Progress
import com.noxcrew.sheeplib.widget.progress.ProgressWidget
import net.minecraft.client.gui.layouts.Layout
import net.minecraft.network.chat.Component
import kotlin.random.Random
import kotlin.random.nextInt

public class ProgressDialog(x: Int, y: Int) : Dialog(x, y), Themed by DefaultTheme {

    private companion object {
        const val WIDTH = 90
        const val HEIGHT = 20
    }

    override val title: DialogTitleWidget? = theme.createTitleWidget(this, "Progress dialog", true)

    override fun layout(): Layout = grid {
        val progress = ProgressWidget(WIDTH, HEIGHT, theme).atBottom(0, 2)

        ThemedButton(
            Component.literal("Waiting"),
            theme = this@ProgressDialog
        ) {
            progress.progress = Progress.Waiting
        }.at(1, 0)
        ThemedButton(Component.literal("Count"), theme = this@ProgressDialog) {
            progress.progress = Progress.InProgress.WithCount(
                "Doing the thing",
                Random.nextInt(0..100),
                100
            )
        }.at(2, 0)

        ThemedButton(Component.literal("Indefinite"), theme = this@ProgressDialog) {
            progress.progress = Progress.InProgress.Indefinite("Doing the thing")
        }.at(2, 1)

        ThemedButton(Component.literal("Complete"), theme = this@ProgressDialog) {
            progress.progress = Progress.Complete
        }.at(3, 0)
        ThemedButton(Component.literal("Failed"), theme = this@ProgressDialog) {
            progress.progress = Progress.Failed(RuntimeException("oops x3"))
        }.at(3, 1)
    }
}
