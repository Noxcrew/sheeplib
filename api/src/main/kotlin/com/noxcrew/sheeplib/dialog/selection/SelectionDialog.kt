package com.noxcrew.sheeplib.dialog.selection

import com.noxcrew.sheeplib.LayoutConstants
import com.noxcrew.sheeplib.dialog.Dialog
import com.noxcrew.sheeplib.dialog.DialogTitleWidget
import com.noxcrew.sheeplib.layout.LinearLayoutBuilder
import com.noxcrew.sheeplib.layout.grid
import com.noxcrew.sheeplib.theme.StaticColorReference
import com.noxcrew.sheeplib.theme.Theme
import com.noxcrew.sheeplib.theme.Themed
import com.noxcrew.sheeplib.theme.ThemedColorReference
import com.noxcrew.sheeplib.util.withOuterPadding
import com.noxcrew.sheeplib.widget.ThemedButton
import com.noxcrew.sheeplib.widget.text.ClickableMultiLineTextWidget
import com.noxcrew.sheeplib.widget.text.ClickableTextWidget
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.layouts.Layout
import net.minecraft.client.gui.layouts.LinearLayout
import net.minecraft.network.chat.Component
import kotlin.math.max

/**
 * A list of options with a title.
 *
 * There are three components to a selection dialog:
 * - the title, a text component at the top of the widget
 * - a row of square buttons below the title, intended to hold icons with longer hover text descriptions
 * - a set of entries, which are full-width text buttons arranged vertically
 *
 * @param x the dialog's X co-ordinate
 * @param y the dialog's Y co-ordinate
 * @param entries a list of entries
 * @param buttons a list of buttons
 * @param titleText the dialog's title
 * @param context the context object, which is provided to entries when called
 * @param theme the dialog's theme. See [Dialog.theme]
 * @param titleSupplier a supplier for the dialog's title widget. See [Dialog.title]
 *
 * @param T a context object. This object is passed to entries when called.
 */
public class SelectionDialog<out T>(
    x: Int, y: Int,
    private val entries: List<Entry<T>>,
    private val buttons: List<Entry<T>> = emptyList(),
    private val titleText: Component,
    private val context: T,
    theme: Theme,
    titleSupplier: (SelectionDialog<T>.() -> DialogTitleWidget)? = null,
) : Dialog(x, y), Themed by theme {

    override val title: DialogTitleWidget? = titleSupplier?.invoke(this)

    /**
     * An entry that can act as either a row entry or a button.
     *
     * @param text the text to display for this entry.
     * If this entry is an icon, this should be a single character, optionally with a detailed hover text.
     * If this entry is a row entry, then the text should be short, again with optional hover text.
     * @param handler the callback function
     * @param T the context type
     */
    public data class Entry<in T>(
        val text: Component, val handler: SelectionDialog<T>.(T) -> Unit
    ) {
        /**
         * @param text a translation key which will be wrapped in [Component.translatable]
         * @param handler the callback function
         */
        public constructor(text: String, handler: SelectionDialog<T>.(T) -> Unit) :
            this(Component.translatable(text), handler)

        public companion object {
            /**
             * Dispatches a command.
             */
            public inline fun <T> dispatchCommand(crossinline provider: (T) -> String): SelectionDialog<T>.(T) -> Unit {
                return {
                    Minecraft.getInstance().connection?.sendCommand(provider(it))
                    close()
                }
            }

            /**
             * Copies a string to the clipboard.
             */
            public inline fun <T> copyToClipboard(crossinline provider: (T) -> String): SelectionDialog<T>.(T) -> Unit {
                return {
                    Minecraft.getInstance().keyboardHandler.clipboard = provider(it)
                }
            }
        }
    }

    private val buttonStyle = Theme.ButtonStyle(
        StaticColorReference(0),
        ThemedColorReference.WIDGET_BACKGROUND_SECONDARY,
        ThemedColorReference.WIDGET_BACKGROUND_SECONDARY,
    )


    init {
        init()
    }

    override fun layout(): Layout = grid {
        val font = Minecraft.getInstance().font
        val lineHeight = font.lineHeight + 2

        val cols = max(1, buttons.size)

        // +1 as most icons have an odd width
        val buttonWidth = theme.dimensions.buttonHeight + 1

        val allButtonsWidth = buttons.size * buttonWidth + theme.dimensions.paddingInner * (buttons.size - 1)

        val titleWidth = if (titleText.string.contains("\n")) {
            ClickableMultiLineTextWidget(titleText, font).atBottom(0, cols)
            0
        } else ClickableTextWidget(titleText, font).atBottom(0, cols).width

        if (buttons.isNotEmpty()) {
            newRow()
            buttons.forEachIndexed { index, (text, action) ->
                ThemedButton(
                    buttonWidth,
                    theme.dimensions.buttonHeight,
                    text,
                    centreText = true,
                    scrollText = false,
                ) {
                    action(context)
                }.onLastRow(index)
            }
        }

        val entryWidth = max(
            titleWidth,
            allButtonsWidth
        )

        LinearLayoutBuilder(
            0, 0, 0,
            lineHeight * entries.size, LinearLayout.Orientation.VERTICAL, 0
        )
            .apply {
                entries.forEach { (text, action) ->
                    +ThemedButton(
                        entryWidth, lineHeight, text,
                        style = buttonStyle, theme = withOuterPadding(theme, 0)
                    ) {
                        action(context)
                    }
                }
            }
            .build()
            .atBottom(0, cols, LayoutConstants.LEFT)
    }
}
