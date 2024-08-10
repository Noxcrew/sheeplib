package com.noxcrew.sheeplib.testmod

import com.noxcrew.sheeplib.coroutines.CoroutineScopeDialog
import com.noxcrew.sheeplib.layout.grid
import com.noxcrew.sheeplib.theme.DefaultTheme
import com.noxcrew.sheeplib.theme.Themed
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.components.StringWidget
import net.minecraft.client.gui.layouts.Layout
import net.minecraft.network.chat.Component
import kotlin.time.Duration.Companion.seconds

/**
 * A coroutine scope dialog that throws an exception in a coroutine.
 */
public class ExceptionCoroutineDialog(x: Int, y: Int) : CoroutineScopeDialog(x, y), Themed by DefaultTheme {
    override fun layout(): Layout = grid {
        launch {
            delay(1.seconds)
            error("Test exception from the exception coroutine dialog. This is not a bug.")
        }

        StringWidget(Component.literal("This dialog will error shortly"), Minecraft.getInstance().font).atBottom(0)
    }
}
