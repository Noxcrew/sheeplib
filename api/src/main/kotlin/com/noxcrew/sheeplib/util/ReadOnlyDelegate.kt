package com.noxcrew.sheeplib.util

import kotlin.reflect.KProperty

public fun interface ReadOnlyDelegate<T> {

    public fun get(property: KProperty<*>): T

    public operator fun getValue(thisRef: Any?, property: KProperty<*>): T = get(property)
}
