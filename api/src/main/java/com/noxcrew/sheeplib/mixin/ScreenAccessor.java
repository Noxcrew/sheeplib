package com.noxcrew.sheeplib.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.ClickEvent;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Screen.class)
public interface ScreenAccessor {
    @Invoker("defaultHandleGameClickEvent")
    public static void sheeplib$defaultHandleGameClickEvent(ClickEvent clickEvent, Minecraft minecraft, @Nullable Screen screen) {
        throw new AssertionError();
    }
}
