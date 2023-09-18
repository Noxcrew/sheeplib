package com.noxcrew.sheeplib.theme

import com.noxcrew.sheeplib.dialog.Dialog
import com.noxcrew.sheeplib.dialog.title.DialogTitleWidget
import com.noxcrew.sheeplib.util.Icon

/**
 * A collection of colours, dimensions, settings, etc. that define a dialog's visual style.
 */
public interface Theme : Themed {

    public override val theme: Theme get() = this

    /** A theme's colour palette. */
    public interface Colors {
        /**
         * The default background colour for dialogs.
         */
        public val dialogBackground: Int

        /**
         * An alternative background colour for dialogs.
         */
        public val dialogBackgroundAlt: Int

        /**
         * The default background colour for widgets.
         */
        public val widgetBackgroundPrimary: Int

        /**
         * A secondary background colour for widgets. Often used for highlighted or disabled widgets.
         */
        public val widgetBackgroundSecondary: Int

        /**
         * The colour to use for window borders.
         */
        public val border: Int

        /**
         * The default text colour.
         */
        public val textPrimary: Int

        /**
         * Analogous to [widgetBackgroundSecondary] but for text.
         */
        public val textSecondary: Int

        /**
         * A colour that represents positivity - "on", "yes", etc. Typically green.
         */
        public val positive: Int

        /**
         * A colour that represents negativity - "off", "no", etc. Typically red.
         */
        public val negative: Int
    }

    /** A collection of dimensions for laying out components. */
    public data class Dimensions(
        /** A standard width for buttons. */
        public val buttonWidth: Int,

        /** A standard height for buttons. */
        public val buttonHeight: Int,

        /** An inner padding, for between components. */
        public val paddingInner: Int,

        /** An outer padding, for dialog edges. */
        public val paddingOuter: Int,
    )

    /**
     * Button styles.
     */
    public data class ButtonStyles(
        /**
         * A standard button style.
         */
        public val standard: ButtonStyle,

        /**
         * A positive button style.
         * @see Colors.positive
         */
        public val positive: ButtonStyle,

        /**
         * A negative button style.
         * @see Colors.negative
         */
        public val negative: ButtonStyle,
    )


    /** Text icons. */
    public interface Icons {
        /** An "open the dropdown" icon. The default impl is a downwards arrow. */
        public val dropdown: Icon

        /** An icon for closing things. The default is a 5x5 cross. */
        public val close: Icon
    }

    /** A style for a button. */
    public data class ButtonStyle(
        /**
         * Background colour when not clicked.
         */
        val defaultColor: ColorReference,
        /**
         * Background colour when hovered.
         */
        val hoverColor: ColorReference,
        /**
         * Background colour when disabled.
         */
        val disabledColor: ColorReference
    )

    /** The theme's dimensions. */
    public val dimensions: Dimensions

    /** The theme's colours. */
    public val colors: Colors

    /** The theme's button styles. */
    public val buttonStyles: ButtonStyles

    /** The theme's dialog borders. */
    public val dialogBorders: Boolean

    /** The theme's icons. */
    public val icons: Icons

    /**
     * Creates a title widget for a dialog.
     *
     * @param dialog the dialog to create a widget for
     * @param text the string text to display (not a component to allow implementations to do their own formatting)
     * @param isCloseable if true, the widget should have a button to [close the dialog][Dialog.close]
     */
    public fun createTitleWidget(dialog: Dialog, text: String, isCloseable: Boolean): DialogTitleWidget? = null

    public companion object {

        public var active: Theme = DefaultTheme
    }

    /**
     * Delegate to this object to use a globally "active" theme. This will usually be [DefaultTheme].
     */
    public object Active : Themed {
        override val theme: Theme get() = active

    }
}

