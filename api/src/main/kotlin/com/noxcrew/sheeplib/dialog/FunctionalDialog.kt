package com.noxcrew.sheeplib.dialog

import com.noxcrew.sheeplib.dialog.title.DialogTitleWidget
import com.noxcrew.sheeplib.layout.grid
import com.noxcrew.sheeplib.theme.Theme
import com.noxcrew.sheeplib.theme.Themed
import com.noxcrew.sheeplib.util.ReadOnlyDelegate
import com.noxcrew.sheeplib.widget.SliderWidget
import dev.triumphteam.nova.ListState
import dev.triumphteam.nova.MutableState
import dev.triumphteam.nova.State
import dev.triumphteam.nova.policy.StateMutationPolicy
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.components.StringWidget
import net.minecraft.client.gui.layouts.Layout
import net.minecraft.network.chat.Component
import org.jetbrains.annotations.ApiStatus
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.KType
import kotlin.reflect.safeCast
import kotlin.reflect.typeOf

/**
 * A functional dialog.
 */
@ApiStatus.NonExtendable
public open class FunctionalDialog<T : FunctionalDialog<T>>(
    x: Int,
    y: Int,
    theme: Themed,
    private val builderFn: FunctionalDialogBuilder<T>.() -> Unit,
) : Dialog(x, y), Themed by theme {

    public sealed interface StateType<S : State> {
        public val stateClass: KClass<S>
        public fun <E : Any> createState(value: E, mutationPolicy: StateMutationPolicy<E>): S

        public data object Simple : StateType<MutableState<*>> {
            override val stateClass: KClass<MutableState<*>> = MutableState::class
            override fun <E : Any> createState(value: E, mutationPolicy: StateMutationPolicy<E>): MutableState<E> =
                MutableState.of(value, mutationPolicy)
        }

        public data object List : StateType<ListState<*>> {
            override val stateClass: KClass<ListState<*>> = ListState::class
            override fun <E : Any> createState(value: E, ignored: StateMutationPolicy<E>): ListState<E> = ListState.of(value)
        }
    }

    @PublishedApi
    internal val states: MutableMap<String, Triple<State, KType, StateMutationPolicy<*>>> = mutableMapOf()

    private lateinit var builder: FunctionalDialogBuilder<T>

    @PublishedApi
    internal fun initInternal() {
        init()
    }

    @PublishedApi
    internal inline fun <reified T : Any, S : State> getState(
        name: String,
        defaultValue: T,
        stateType: StateType<S>,
        mutationPolicy: StateMutationPolicy<T>,
        rerenderOnChange: Boolean,
    ): S {
        states[name]?.let { (state, type, stateMutationPolicy) ->
            check(type == typeOf<T>()) { "Conflicting type for state $name - expected ${typeOf<T>()} but found $type" }
            check(stateMutationPolicy == mutationPolicy) { "Conflicting mutation policy for state $name - expected $mutationPolicy but found $stateMutationPolicy" }
            return stateType.stateClass.safeCast(state) ?: error {
                "Conflicting state type for state $name - expected ${stateType.stateClass.simpleName} but found ${state::class.simpleName}"
            }
        }

        return stateType.createState(defaultValue, mutationPolicy).also {
            states[name] = Triple(it, typeOf<T>(), mutationPolicy)

            if (rerenderOnChange) {
                it.addListener(this@FunctionalDialog, ::initInternal)
            }
        }
    }

    override val title: DialogTitleWidget?
        get() = builder.titleWidget

    override fun layout(): Layout {
        builder = FunctionalDialogBuilder(this as T).also(builderFn)
        return builder.layout
    }
}

public class FunctionalDialogBuilder<T : FunctionalDialog<*>>(
    public val dialog: T,
) : Themed by dialog {
    public lateinit var layout: Layout
    public var titleWidget: DialogTitleWidget? = null

    public inline fun <reified T : Any> state(
        defaultValue: T,
        mutationPolicy: StateMutationPolicy<T> = StateMutationPolicy.StructuralEquality(),
        rerenderOnChange: Boolean = false,
    ): ReadOnlyDelegate<MutableState<T>> = ReadOnlyDelegate {
        dialog.getState(
            it.name,
            defaultValue,
            FunctionalDialog.StateType.Simple,
            mutationPolicy,
            rerenderOnChange
        ) as MutableState<T>
    }

    public inline fun <reified T : Any> listState(
        vararg defaultValues: T,
        rerenderOnChange: Boolean = false,
    ): ReadOnlyDelegate<ListState<T>> = ReadOnlyDelegate {
        dialog.getState(
            it.name,
            defaultValues,
            FunctionalDialog.StateType.List,
            StateMutationPolicy.StructuralEquality(),
            rerenderOnChange
        ) as ListState<T>
    }

    public inline fun <T, S> T.bind(state: MutableState<S>, crossinline receiver: (T) -> Unit): T = also {
        state.addListener(this@FunctionalDialogBuilder.dialog) {
            receiver(this)
        }
    }

    public fun <T, S> T.bind(state: MutableState<S>, property: KMutableProperty1<T, S>): T = also {
        bind(state) { property.setter.call(state.get()) }
    }
}

public val <S> MutableState<S>.setter: (S) -> Unit
    get() = { it: S ->
        this.set(it)
    }

public fun testFunctionalDialog(x: Int, y: Int): FunctionalDialog<*> = FunctionalDialog(x, y, Theme.Active) {
    val count by state(0)

    layout = grid {
        StringWidget(Component.literal(count.get().toString()), Minecraft.getInstance().font)
            .atBottom(0)
            .bind(count) { it.message = Component.literal(count.get().toString()) }

        SliderWidget(100, 0, 9, this@FunctionalDialog, updateCallback = count.setter).atBottom(0)
    }
}

