package com.noxcrew.sheeplib.coroutines

import com.noxcrew.sheeplib.dialog.Dialog
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import org.jetbrains.annotations.ApiStatus
import org.jetbrains.annotations.MustBeInvokedByOverriders
import kotlin.coroutines.CoroutineContext

/** A dialog with its own [CoroutineContext], which is cancelled when the dialog is closed. */
@ApiStatus.Experimental
public abstract class CoroutineScopeDialog(
    x: Int,
    y: Int,
    dispatcher: CoroutineDispatcher = MinecraftDispatchers.Background
) : Dialog(x, y),
    CoroutineScope {

    override val coroutineContext: CoroutineContext by lazy { SupervisorJob() + CoroutineName("$this") + dispatcher }

    /** Cancels the coroutine context. Must be invoked by overriders. */
    @MustBeInvokedByOverriders
    override fun onClose() {
        coroutineContext.cancel(DialogClosedCancellationException)
    }
}

/** A cancellation exception that indicates that a job's [CoroutineScopeDialog] was closed. */
public object DialogClosedCancellationException: CancellationException()
