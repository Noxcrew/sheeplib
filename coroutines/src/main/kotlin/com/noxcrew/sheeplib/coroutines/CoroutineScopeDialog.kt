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
    CoroutineScope {
    override val coroutineContext: CoroutineContext by lazy {
        SupervisorJob() +
                CoroutineName("$this") +
                dispatcher +
                CoroutineExceptionHandler { _, ex ->
                    Minecraft.getInstance().gui.chat.addMessage(
                        Component.translatable("sheeplib.error.coroutine").withStyle { it.withColor(ChatFormatting.RED) }
                    )
                    LoggerFactory.getLogger("SheepLib").error("Exception caught in dialog coroutine scope (${this::class.jvmName}):\n" + ex.stackTraceToString())
                    close()
                }
    }

    /** Cancels the coroutine context. Must be invoked by overriders. */
    @MustBeInvokedByOverriders
    override fun onClose() {
        coroutineContext.cancel(DialogClosedCancellationException)
    }
}

/** A cancellation exception that indicates that a job's [CoroutineScopeDialog] was closed. */
public object DialogClosedCancellationException : CancellationException()
