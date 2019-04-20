package com.github.rougsig.rxflux.dsl

import com.github.rougsig.rxflux.core.action.Action
import com.github.rougsig.rxflux.core.action.ScopedAction
import com.github.rougsig.rxflux.core.reducer.Mutator
import com.github.rougsig.rxflux.core.reducer.MutatorImpl
import com.github.rougsig.rxflux.core.reducer.Reducer
import com.github.rougsig.rxflux.core.reducer.ReducerImpl
import kotlin.reflect.KClass

open class ReducerBuilder<S : Any, A : Action>(
  private val type: KClass<A>,
  private val initialState: S
) {
  private val mutators = mutableListOf<Mutator<S, Action>>()

  fun <MA : A> mutator(
    type: KClass<MA>,
    mutator: (S, MA) -> S
  ): ReducerBuilder<S, A> {
    mutators.add(MutatorImpl(type, mutator))
    return this
  }

  internal fun build(): Reducer<S, Action> {
    return ReducerImpl(
      type,
      initialState,
      mutators
    )
  }
}

abstract class ConfigurableReducer<S : Any>(
  type: KClass<Action>,
  initialState: S,
  val namespace: String
) : Reducer<S, Action>, ReducerBuilder<S, Action>(type, initialState) {
  private val reducer = this.build()

  final override fun reduce(state: S?, action: Action): S {
    return reducer.reduce(state, action)
  }

  protected fun createAction(creator: () -> Action): Action {
    return ScopedAction(creator.invoke(), namespace)
  }
}
