package com.noxcrew.sheeplib.util

import net.minecraft.util.FastColor.ARGB32

/** The left shift necessary for the alpha component of an ARGB colour. */
public const val OPACITY_SHIFT: Int = 24

/** A bitmask for the alpha component of an ARGB colour. */
public const val OPAQUE_BITMASK: Int = 255 shl OPACITY_SHIFT

/** Returns this number as a 32-bit ARGB colour, with an alpha of 255. */
public fun Int.opaqueColor(): Int = this or OPAQUE_BITMASK

/** Returns this number as a 32-bit ARGB colour, with an alpha of defined in [opacity]. */
public infix fun Int.opacity(opacity: Int): Int = this or (opacity shl OPACITY_SHIFT)

/** Lightens a 32-bit ARGB colour by a percentage defined by [lightenBy]. */
public infix fun Int.lighten(lightenBy: Float): Int = ARGB32.color(
    ARGB32.alpha(this),
    (ARGB32.red(this) * (lightenBy + 1)).toInt().coerceIn(0, 0xff),
    (ARGB32.green(this) * (lightenBy + 1)).toInt().coerceIn(0, 0xff),
    (ARGB32.blue(this) * (lightenBy + 1)).toInt().coerceIn(0, 0xff),
)
