package com.noxcrew.sheeplib.dialog

import com.noxcrew.sheeplib.dialog.title.DialogTitleWidget
import com.noxcrew.sheeplib.theme.Themed
import net.minecraft.client.gui.layouts.Layout
import java.io.Closeable

public interface IDialog : Themed, Closeable {

    /** The dialog's parent. */
    public val parent: Dialog?

    /** The dialog's state. */
    public val state: State

    /**
     * A child dialog to show over the top of this one.
     */
    public val popup: Dialog?

    public val title: DialogTitleWidget?

    /**
     * Opens a dialog as a popup.
     *
     * @see [popup]
     */
    public fun popup(dialog: Dialog, replace: Boolean = false)

    /**
     * Gets whether this dialog has a popup open.
     */
    public fun isPopupFocused(): Boolean
    public fun setX(i: Int)
    public fun setY(i: Int)
    public fun layout(): Layout
    public fun titleHeight(): Int

    /** A dialog's state. */
    public enum class State(public val isClosing: Boolean) {
        /** The dialog has been created but not yet initialised. */
        READY(false),

        /** The dialog has been initialised and is ready to be used. */
        ACTIVE(false),

        /** The dialog is being closed. */
        CLOSING(true),

        /** The dialog has been closed. */
        CLOSED(true),
        ;
    }
}
