package com.noxcrew.sheeplib.coroutines

import com.noxcrew.sheeplib.dialog.IDialog
import kotlinx.coroutines.CoroutineScope
import org.jetbrains.annotations.MustBeInvokedByOverriders

/**
 * A dialog with its own CoroutineContext, which is cancelled when the dialog is closed.
 */
public interface ICoroutineScopeDialog : IDialog, CoroutineScope {

    /** Cancels the coroutine context. Must be invoked by overriders. */
    @MustBeInvokedByOverriders
    public fun onClose()
}
