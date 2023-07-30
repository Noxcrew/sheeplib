package com.noxcrew.sheeplib.layout

import net.minecraft.client.gui.layouts.Layout

@DslMarker
@Target(AnnotationTarget.CLASS, AnnotationTarget.TYPE)
public annotation class LayoutBuilderDsl

/**
 * A fancy DSL for putting stuff into layouts.
 */
@LayoutBuilderDsl
public fun interface LayoutBuilder<T : Layout> {

    /**
     * Builds this layout.
     */
    public fun build(): T
}

