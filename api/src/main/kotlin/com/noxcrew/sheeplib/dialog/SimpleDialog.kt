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
    override val theme: Theme = DefaultTheme,
    builderFn: Builder.() -> Unit
) :
    Dialog(x, y),
    Themed {

    // This stops intellij from complaining about the final
    // which is obviously not very useful given this is a final class,
    // but it stops intellij complaining about something else
    @Suppress("RedundantModalityModifier")
    final override val title: DialogTitleWidget?
    private var builtLayout: Layout
    override fun layout(): Layout = builtLayout

    init {
        val builder = Builder(this, null)
        builder.builderFn()
        builder.check()
        title = builder.title
        builtLayout = builder.layout

    }

    /**
     * Builds a [SimpleDialog].
     * @param dialog The dialog that's being built.
     * @param title The dialog's title.
     */
    public data class Builder(public val dialog: SimpleDialog, public var title: DialogTitleWidget?) {
        /**
         * The dialog's layout.
         */
        public lateinit var layout: Layout

        /**
         * Checks that the builder has been fully completed before building.
         */
        public fun check() {
            check(::layout.isInitialized) { "No layout has been set for SimpleDialog" }
        }
    }
}
