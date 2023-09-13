package com.noxcrew.sheeplib.util

import com.noxcrew.sheeplib.layout.LayoutBuilderDsl
import net.minecraft.network.chat.Component

/**
 *  A DSL object that can be used to build collections of labelled buttons.
 *  @param T the type of callback for each button
 */
@LayoutBuilderDsl
public class TextButtonCollectionBuilder<T : Function<*>> {

    @PublishedApi
    internal val elements: MutableList<Pair<Component, T>> = mutableListOf()

    /**
     * Adds a button with string literal text.
     * @param callback the function to execute when the button is clicked
     */
    public infix fun String.literal(callback: T): Unit =
        Component.literal(this) runs callback

    /**
     * Adds a button with translatable text with the given key.
     * @param callback the function to execute when the button is clicked
     */
    public infix fun String.runs(callback: T): Unit =
        Component.translatable(this) runs callback

    /**
     * Adds a button with some given text.
     * @param callback the function to execute when the button is clicked
     */
    public infix fun Component.runs(callback: T) {
        elements += this to callback
    }
}
