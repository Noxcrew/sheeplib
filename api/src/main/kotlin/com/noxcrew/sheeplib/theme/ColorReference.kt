package com.noxcrew.sheeplib.theme

/** A reference to a colour. */
public sealed interface ColorReference {
    /** Gets this colour as a 32-bit ARGB integer. */
    public fun get(theme: Theme): Int
}

/** A reference to the literal colour [color]. */
public data class StaticColorReference(public val color: Int) : ColorReference {
    override fun get(theme: Theme): Int = color
}

/**
 * A reference to a colour as part of a theme.
 * @see Theme.Colors
 */
public enum class ThemedColorReference : ColorReference {
    TEXT_PRIMARY,
    TEXT_SECONDARY,
    DIALOG_BACKGROUND,
    DIALOG_BACKGROUND_ALT,
    WIDGET_BACKGROUND_PRIMARY,
    WIDGET_BACKGROUND_SECONDARY,
    BORDER,
    POSITIVE,
    NEGATIVE;

    override fun get(theme: Theme): Int = theme.colors[this]

    public companion object {

        /** Gets a colour from the theme by its [ThemedColorReference]. */
        public operator fun Theme.Colors.get(key: ThemedColorReference): Int = when (key) {
            TEXT_PRIMARY -> textPrimary
            TEXT_SECONDARY -> textSecondary
            DIALOG_BACKGROUND -> dialogBackground
            DIALOG_BACKGROUND_ALT -> dialogBackgroundAlt
            WIDGET_BACKGROUND_PRIMARY -> widgetBackgroundPrimary
            WIDGET_BACKGROUND_SECONDARY -> widgetBackgroundSecondary
            BORDER -> border
            POSITIVE -> positive
            NEGATIVE -> negative
        }
    }
}
