package com.noxcrew.sheeplib.dialog.selection

import com.noxcrew.sheeplib.dialog.selection.SelectionDialog.Entry
import com.noxcrew.sheeplib.dialog.title.DialogTitleWidget
import com.noxcrew.sheeplib.theme.Theme
import net.minecraft.network.chat.Component
import org.jetbrains.annotations.ApiStatus.ScheduledForRemoval

/**
 * Builds a [SelectionDialog].
 */
@Deprecated("See SelectionDialog")
@ScheduledForRemoval(inVersion = "1.0.0")
public class SelectionDialogBuilder<T> {

    private val entries: MutableList<Entry<T>> = mutableListOf()
    private val buttons: MutableList<Entry<T>> = mutableListOf()

    /** Adds the receiver as a button. */
    public fun Entry<T>.button() {
        buttons.add(this)
    }

    /** Adds the receiver as an entry. */
    public fun Entry<T>.entry() {
        entries.add(this)
    }

    /**
     * Builds a dialog.
     * @see SelectionDialog
     */
    public fun build(
        x: Int,
        y: Int,
        titleText: Component,
        context: T,
        theme: Theme,
        title: (SelectionDialog<T>.() -> DialogTitleWidget)?
    ): SelectionDialog<T> =
        SelectionDialog(x, y, entries, buttons, titleText, context, theme, title)

}

/**
 * Creates a [SelectionDialog] from a builder.
 * @see SelectionDialog
 */
@Deprecated("See SelectionDialog")
@ScheduledForRemoval(inVersion = "1.0.0")
public inline fun <T> selectionDialog(
    x: Int,
    y: Int,
    titleText: Component,
    context: T,
    theme: Theme,
    noinline dialogTitleWidget: (SelectionDialog<T>.() -> DialogTitleWidget)? = null,
    builder: SelectionDialogBuilder<T>.() -> Unit,
): SelectionDialog<T> =
    SelectionDialogBuilder<T>().also(builder).build(x, y, titleText, context, theme, dialogTitleWidget)
