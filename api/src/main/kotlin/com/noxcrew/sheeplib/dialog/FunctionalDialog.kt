package com.noxcrew.sheeplib.dialog

import com.noxcrew.sheeplib.layout.grid
import com.noxcrew.sheeplib.theme.Theme
import com.noxcrew.sheeplib.theme.Themed
import com.noxcrew.sheeplib.util.ReadOnlyDelegate
import com.noxcrew.sheeplib.widget.SliderWidget
import dev.triumphteam.nova.MutableState
import dev.triumphteam.nova.Stateful
import dev.triumphteam.nova.builtin.SimpleMutableState
import dev.triumphteam.nova.policy.StateMutationPolicy
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.components.StringWidget
import net.minecraft.client.gui.layouts.Layout
import net.minecraft.network.chat.Component
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.KType
import kotlin.reflect.typeOf

/**
 * A functional dialog.
 */
public class FunctionalDialog(
    x: Int,
    y: Int,
    theme: Themed,
    private val builder: FunctionalDialogBuilder.() -> Unit,
) : Dialog(x, y), Stateful, Themed by theme {

    @PublishedApi
    internal val states: MutableMap<String, Triple<MutableState<*>, KType, StateMutationPolicy>> = mutableMapOf()

    @PublishedApi
    internal fun initInternal() {
        init()
    }

    @PublishedApi
    internal inline fun <reified T : Any> getState(
        name: String,
        defaultValue: T,
        mutationPolicy: StateMutationPolicy,
        rerenderOnChange: Boolean,
    ): MutableState<T> {
        states[name]?.let { (state, type, stateMutationPolicy) ->
            check(type == typeOf<T>()) { "Conflicting type for state $name - expected ${typeOf<T>()} but found $type" }
            check(stateMutationPolicy == mutationPolicy) { "Conflicting mutation policy for state $name - expected $mutationPolicy but found $stateMutationPolicy" }
            @Suppress("UNCHECKED_CAST")
            return state as MutableState<T>
        }

        return SimpleMutableState(defaultValue, mutationPolicy).also {
            states[name] = Triple(it, typeOf<T>(), mutationPolicy)

            if (rerenderOnChange) {
                it.addListener(this@FunctionalDialog, ::initInternal)
            }
        }
    }

    override fun layout(): Layout = FunctionalDialogBuilder(this).also(builder).layout
}

public class FunctionalDialogBuilder(
    @PublishedApi
    internal val dialog: FunctionalDialog,
) : Themed by dialog {
    public lateinit var layout: Layout

    public inline fun <reified T : Any> state(
        defaultValue: T,
        mutationPolicy: StateMutationPolicy = StateMutationPolicy.StructuralEquality.INSTANCE,
        rerenderOnChange: Boolean = false,
    ): ReadOnlyDelegate<MutableState<T>> = ReadOnlyDelegate {
        dialog.getState(it.name, defaultValue, mutationPolicy, rerenderOnChange)
    }

    public inline fun <T, S> T.bind(state: MutableState<S>, crossinline receiver: (T) -> Unit): T = also {
        state.addListener(this@FunctionalDialogBuilder.dialog) {
            receiver(this)
        }
    }

    public fun <T, S> T.bind(state: MutableState<S>, property: KMutableProperty1<T, S>): T = also {
        bind(state) { property.setter.call(state.value) }
    }
}

public val <S> MutableState<S>.setter: (S) -> Unit
    get() = { it: S ->
        this.value = it
    }

public fun testFunctionalDialog(x: Int, y: Int): FunctionalDialog = FunctionalDialog(x, y, Theme.Active) {
    val count by state(0)

    layout = grid {
        StringWidget(Component.literal(count.value.toString()), Minecraft.getInstance().font)
            .atBottom(0)
            .bind(count) { it.message = Component.literal(count.value.toString()) }

        SliderWidget(100, 0, 9, this@FunctionalDialog, updateCallback = count.setter).atBottom(0)
    }
}

