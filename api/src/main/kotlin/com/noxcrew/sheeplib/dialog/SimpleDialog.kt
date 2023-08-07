package com.noxcrew.sheeplib.dialog

import com.noxcrew.sheeplib.dialog.title.DialogTitleWidget
import com.noxcrew.sheeplib.theme.DefaultTheme
import com.noxcrew.sheeplib.theme.Theme
import com.noxcrew.sheeplib.theme.Themed
import net.minecraft.client.gui.layouts.Layout
import org.jetbrains.annotations.ApiStatus

/**
 * A basic, stateless dialog. Use this instead of subclassing if you don't need to keep track of state.
 */
@ApiStatus.Experimental
public class SimpleDialog(
    x: Int, y: Int,
    titleProducer: (Dialog) -> DialogTitleWidget?,
    private val dialogLayout: Layout,
    override val theme: Theme = DefaultTheme,
) :
    Dialog(x, y),
    Themed {

    // This stops intellij from complaining about the final
    // which is obviously not very useful given this is a final class,
    // but it stops intellij complaining about something else
    @Suppress("RedundantModalityModifier")
    final override val title: DialogTitleWidget?
    override fun layout(): Layout = dialogLayout

    init {
        title = titleProducer(this)
    }
}
