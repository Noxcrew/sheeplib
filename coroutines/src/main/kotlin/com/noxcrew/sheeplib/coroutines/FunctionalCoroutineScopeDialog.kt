package com.noxcrew.sheeplib.coroutines

import com.noxcrew.sheeplib.dialog.FunctionalDialog
import com.noxcrew.sheeplib.dialog.FunctionalDialogBuilder
import com.noxcrew.sheeplib.theme.Themed
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.cancel
import org.jetbrains.annotations.MustBeInvokedByOverriders
import kotlin.coroutines.CoroutineContext

public class FunctionalCoroutineScopeDialog(
    x: Int,
    y: Int,
    theme: Themed,
    dispatcher: CoroutineDispatcher = MinecraftDispatchers.Background,
    builder: FunctionalDialogBuilder<FunctionalCoroutineScopeDialog>.() -> Unit,
) : FunctionalDialog<FunctionalCoroutineScopeDialog>(x, y, theme, builder), ICoroutineScopeDialog {
    override val coroutineContext: CoroutineContext by CoroutineScopeDialog.createContext(this, dispatcher)

    /** Cancels the coroutine context. Must be invoked by overriders. */
    @MustBeInvokedByOverriders
    override fun onClose() {
        coroutineContext.cancel(DialogClosedCancellationException)
    }
}
