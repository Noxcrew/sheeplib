package com.noxcrew.sheeplib.coroutines

import com.noxcrew.sheeplib.dialog.Dialog
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import net.minecraft.ChatFormatting
import net.minecraft.client.Minecraft
import net.minecraft.network.chat.Component
import org.jetbrains.annotations.ApiStatus
import org.jetbrains.annotations.MustBeInvokedByOverriders
import org.slf4j.LoggerFactory
import kotlin.coroutines.CoroutineContext
import kotlin.reflect.jvm.jvmName

/** A dialog with its own [CoroutineContext], which is cancelled when the dialog is closed. */
@ApiStatus.Experimental
public abstract class CoroutineScopeDialog(
    x: Int,
    y: Int,
    dispatcher: CoroutineDispatcher = MinecraftDispatchers.Background,
) : Dialog(x, y),
    CoroutineScope, ICoroutineScopeDialog {
    override val coroutineContext: CoroutineContext by createContext(this, dispatcher)

    /** Cancels the coroutine context. Must be invoked by overriders. */
    @MustBeInvokedByOverriders
    override fun onClose() {
        coroutineContext.cancel(DialogClosedCancellationException)
    }

    internal companion object {
        internal fun <T> createContext(dialog: T, dispatcher: CoroutineDispatcher) where T : ICoroutineScopeDialog, T : Dialog =
            lazy {
                SupervisorJob() +
                        CoroutineName("$dialog") +
                        dispatcher +
                        CoroutineExceptionHandler { _, ex ->
                            Minecraft.getInstance().gui.chat.addMessage(
                                Component.translatable("sheeplib.error.coroutine")
                                    .withStyle { it.withColor(ChatFormatting.RED) }
                            )
                            LoggerFactory.getLogger("SheepLib")
                                .error("Exception caught in dialog coroutine scope (${dialog::class.jvmName}):\n" + ex.stackTraceToString())
                            dialog.close()
                        }
            }
    }
}

/** A cancellation exception that indicates that a job's [CoroutineScopeDialog] was closed. */
public object DialogClosedCancellationException : CancellationException()
