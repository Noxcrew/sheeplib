package com.noxcrew.sheeplib.widget

import com.noxcrew.sheeplib.theme.Theme
import com.noxcrew.sheeplib.theme.Themed
import net.minecraft.network.chat.Component
import kotlin.reflect.KProperty

/**
 * A button that cycles between a list of entries.
 */
public class ToggleButton<T>(
    private val entries: List<Entry<T>>,
    private var currentIndex: Int = 0,
    private val prefix: Component? = null,
    theme: Themed,
    private val changeHandler: ((T) -> Unit)?
) :
    ThemedButton(Component.empty(), theme = theme, clickHandler = { }) {
    public data class Entry<T>(val contents: T, val text: Component, val style: Theme.ButtonStyle?) {
        public constructor(contents: T, text: String, style: Theme.ButtonStyle?) : this(
            contents,
            Component.translatable(text),
            style
        )
    }

    private lateinit var _message: Component

    public var current: Entry<T> =
        entries.getOrNull(currentIndex) ?: throw IllegalArgumentException("Entries must not be empty")
        private set(value) {
            field = value
            _message = prefix?.copy()?.append(value.text) ?: value.text
        }


    /** Delegates to [current.contents][Entry.contents]. */
    public operator fun getValue(thisRef: Any?, property: KProperty<*>): T = current.contents

    override fun getMessage(): Component = _message

    init {
        current = current
    }

    override val style: Theme.ButtonStyle get() = current.style ?: super.style

    override fun onClick(d: Double, e: Double) {
        if (!isEnabled) return
        currentIndex = (currentIndex + 1) % entries.size
        current = entries[currentIndex]
        changeHandler?.invoke(current.contents)
    }

    public companion object {
        /**
         * Creates a new [ToggleButton] that toggles a boolean on and off.
         *
         * @param isOn the default state
         * @param prefix the text to show before the word "On" or "Off"
         * @param theme the theme to use for the button
         * @param changeHandler a callback for when the button is clicked
         */
        public fun onOff(
            isOn: Boolean = false,
            prefix: Component? = null,
            theme: Themed = Theme.Active,
            changeHandler: ((Boolean) -> Unit)? = null
        ): ToggleButton<Boolean> = ToggleButton(
            listOf(
                Entry(true, "sheeplib.toggle.on", theme.theme.buttonStyles.positive),
                Entry(false, "sheeplib.toggle.off", theme.theme.buttonStyles.negative)
            ),
            if (isOn) 0 else 1,
            prefix,
            theme,
            changeHandler
        )

        /**
         * Creates a new [ToggleButton] that cycles between the values of an enum.
         *
         * @param translatablePrefix a prefix to use for translatable keys for each value.
         * These keys will be joined with each enum value's name lowercased, separated by a dot.
         * @param prefix the text to show before the enum's name
         * @param theme the theme to use for the button
         * @param changeHandler a callback for when the button is clicked and a new value is set
         */
        public inline fun <reified T : Enum<T>> enum(
            translatablePrefix: String,
            prefix: Component? = null,
            theme: Themed = Theme.Active,
            noinline changeHandler: (T) -> Unit
        ): ToggleButton<T> = ToggleButton(
            enumValues<T>().map {
                Entry(
                    it,
                    "$translatablePrefix.${it.name.lowercase()}",
                    theme.theme.buttonStyles.standard
                )
            },
            0,
            prefix,
            theme,
            changeHandler
        )

    }
}
