package com.noxcrew.sheeplib.util

import net.minecraft.network.chat.ClickEvent
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.FontDescription
import net.minecraft.network.chat.HoverEvent
import net.minecraft.network.chat.MutableComponent
import net.minecraft.network.chat.TextColor

@DslMarker @Target(AnnotationTarget.CLASS)
private annotation class ComponentBuilderDsl

/**
 * A builder for a [Component].
 */
@ComponentBuilderDsl
public class ComponentBuilder(public val component: MutableComponent) {

    /**
     * The component's text colour.
     */
    public var color: TextColor?
        get() = component.style.color
        set(value) {
            component.style = component.style.withColor(value)
        }

    /**
     * Whether the component is bold.
     */
    public var bold: Boolean
        get() = component.style.isBold
        set(value) {
            component.style = component.style.withBold(value)
        }

    /**
     * Whether the component is italic.
     */
    public var italic: Boolean
        get() = component.style.isItalic
        set(value) {
            component.style = component.style.withItalic(value)
        }


    /**
     * Whether the component is underlined.
     */
    public var underline: Boolean
        get() = component.style.isUnderlined
        set(value) {
            component.style = component.style.withUnderlined(value)
        }

    /**
     * The component's hover event.
     */
    public var hoverEvent: HoverEvent?
        get() = component.style.hoverEvent
        set(value) {
            component.style = component.style.withHoverEvent(value)
        }

    /**
     * The component's click event.
     */
    public var clickEvent: ClickEvent?
        get() = component.style.clickEvent
        set(value) {
            component.style = component.style.withClickEvent(value)
        }

    /**
     * The component's insertion string.
     */
    public var insertion: String?
        get() = component.style.insertion
        set(value) {
            component.style = component.style.withInsertion(value)
        }

    /**
     * The component's font.
     */
    public var font: FontDescription?
        get() = component.style.font
        set(value) {
            component.style = component.style.withFont(value)
        }

    /**
     * Adds this component as a child of the component being built.
     */
    public operator fun Component.unaryPlus() {
        component.append(this)
    }

    /**
     * Adds this string, wrapped in a literal component, as a child of the component being built.
     *
     * @see [Component.unaryPlus]
     */
    public operator fun String.unaryPlus() {
        component.append(this)
    }

    /**
     * Adds a space literal to the component.
     */
    public fun space() {
        +" "
    }

    /**
     * Adds a newline literal to the component.
     */
    public fun newline() {
        +"\n"
    }

    public companion object {

        /**
         * Applies the builder function [builder] to [contents].
         *
         * Be careful with this method as the builder will mutate the underlying style.
         * Consider cloning the component before passing in here.
         */
        public inline fun apply(contents: MutableComponent, builder: ComponentBuilder.() -> Unit): Component =
            ComponentBuilder(contents).also(builder).component

        /**
         * Applies [builder] to a new literal component with the contents [contents].
         */
        public inline fun literal(contents: String, builder: ComponentBuilder.() -> Unit): Component =
            apply(Component.literal(contents), builder)

        /**
         * Applies [builder] to a new translatable component with key [key] and arguments [objects].
         */
        public inline fun translatable(
            key: String,
            vararg objects: Any,
            builder: ComponentBuilder.() -> Unit
        ): Component =
            apply(Component.translatable(key, *objects), builder)

        /**
         * Applies [builder] to a new empty component.
         */
        public inline fun build(builder: ComponentBuilder.() -> Unit): Component = apply(Component.empty(), builder)
    }
}
