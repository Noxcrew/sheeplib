package com.noxcrew.sheeplib.coroutines

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Runnable
import kotlinx.coroutines.asCoroutineDispatcher
import net.minecraft.client.Minecraft
import net.minecraft.util.Util
import kotlin.coroutines.CoroutineContext


/** Some coroutine dispatchers, backed by Minecraft's own thread pools. */
public object MinecraftDispatchers {

    /** A coroutine dispatcher, backed by [Util.backgroundExecutor]. */
    public val Background: CoroutineDispatcher = Util.backgroundExecutor().asCoroutineDispatcher()

    /** A coroutine dispatcher, backed by [Util.ioPool]. Use this for blocking operations. */
    public val IO: CoroutineDispatcher = Util.ioPool().asCoroutineDispatcher()

    /**
     * A coroutine dispatcher that runs coroutines on the render thread.
     */
    public val Main: CoroutineDispatcher = object : CoroutineDispatcher() {
        override fun isDispatchNeeded(context: CoroutineContext): Boolean = !Minecraft.getInstance().isSameThread

        override fun dispatch(context: CoroutineContext, block: Runnable) {
            Minecraft.getInstance().execute(block)
        }
    }
}
