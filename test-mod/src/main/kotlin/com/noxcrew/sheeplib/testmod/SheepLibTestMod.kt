package com.noxcrew.sheeplib.testmod

import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.noxcrew.sheeplib.DialogContainer
import com.noxcrew.sheeplib.dialog.Dialog
import com.noxcrew.sheeplib.dialog.PromptDialog
import com.noxcrew.sheeplib.theme.Theme
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.minecraft.network.chat.Component

/** A test mod for SheepLib. */
public object SheepLibTestMod : ClientModInitializer {

    /**
     * A map of dialog names that can be opened through the command.
     */
    private val dialogs = mapOf<String, (Int, Int) -> Dialog>(
        "prompt" to { x, y ->
            PromptDialog(
                x, y,
                Theme.Active,
                Component.literal("Title"),
                Component.literal("Prompt dialog body"),
            )
        },
    )


    private val COMMAND: LiteralArgumentBuilder<FabricClientCommandSource> = ClientCommandManager.literal("sheeplib")
        .then(ClientCommandManager.literal("open").then(
            ClientCommandManager.argument("dialog", StringArgumentType.string())
                .suggests { _, builder ->
                    dialogs.keys.forEach(builder::suggest)
                    builder.buildFuture()
                }
                .executes {
                    dialogs[it.getArgument("dialog", String::class.java)]?.let {
                        DialogContainer += it(10, 10)
                    }
                    0
                }
        ))

    override fun onInitializeClient() {
        ClientCommandRegistrationCallback.EVENT.register { dispatcher, _ ->
            dispatcher.register(COMMAND)
        }
    }
}

