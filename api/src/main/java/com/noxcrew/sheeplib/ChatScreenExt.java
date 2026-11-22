package com.noxcrew.sheeplib;

/**
 * An extension to {@link net.minecraft.client.gui.screens.ChatScreen} that keeps its focus in sync with
 * the {@link DialogContainer}.
 */
public interface ChatScreenExt {
    /**
     * Focuses the dialog container while a widget in the focused dialog takes text input,
     * and the chat input otherwise.
     */
    default void sheeplib$syncFocus() {
        throw new AssertionError("Implemented in Mixin");
    }
}
