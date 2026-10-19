package com.noxcrew.sheeplib

import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.screens.ChatScreen
import net.minecraft.resources.Identifier

public class SheepLibClient : ClientModInitializer {
    override fun onInitializeClient() {
        HudElementRegistry.attachElementBefore(
            VanillaHudElements.CHAT,
            Identifier.fromNamespaceAndPath("sheeplib", "before_chat")
        ) { guiGraphics, deltaTracker ->
            if (Minecraft.getInstance().gui.screen() !is ChatScreen) {
                DialogContainer.extractRenderState(guiGraphics, 0, 0, deltaTracker.gameTimeDeltaTicks)
            }
        }
    }
}