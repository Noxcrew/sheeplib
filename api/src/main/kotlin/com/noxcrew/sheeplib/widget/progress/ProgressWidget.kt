package com.noxcrew.sheeplib.widget.progress

import com.noxcrew.sheeplib.CompoundWidget
import com.noxcrew.sheeplib.layout.linear
import com.noxcrew.sheeplib.theme.Themed
import com.noxcrew.sheeplib.widget.text.ClickableTextWidget
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.layouts.Layout
import net.minecraft.client.gui.layouts.LinearLayout
import net.minecraft.network.chat.Component
import org.jetbrains.annotations.ApiStatus

/**
 * A widget that displays progress with a text string and a progress bar.
 */
@ApiStatus.Experimental
public class ProgressWidget(width: Int, height: Int, theme: Themed) : CompoundWidget(0, 0, width, height),
    Themed by theme {

    private companion object {
        private const val PROGRESS_BAR_WIDTH = 2
        private val Progress.text
            get() = when (this) {
                is Progress.Waiting -> Component.translatable("sheeplib.progress.waiting")
                is Progress.InProgress.Indefinite -> Component.translatable("sheeplib.progress.inProgress")
                is Progress.InProgress.WithCount -> Component.translatable(
                    "sheeplib.progress.inProgress.count",
                    done,
                    total
                )
                is Progress.Complete -> Component.translatable("sheeplib.progress.complete")
                is Progress.Failed -> Component.translatable("sheeplib.progress.failed")
            }
    }

    private val progressLock = Any()
    public var progress: Progress = Progress.Waiting
        set(value) = synchronized(progressLock) {
            field = value
            text.message = value.text
        }

    private val font = Minecraft.getInstance().font

    private val progressBar = ProgressBar(width, PROGRESS_BAR_WIDTH, this)

    private val text = ClickableTextWidget(progress.text, font)

    override val layout: Layout = linear(
        width,
        font.lineHeight + theme.theme.dimensions.paddingInner + PROGRESS_BAR_WIDTH,
        LinearLayout.Orientation.VERTICAL
    ) {
        +text
        +progressBar
    }
}
