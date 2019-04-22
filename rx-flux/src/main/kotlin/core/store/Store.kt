package com.github.rougsig.rxflux.core.store

import com.github.rougsig.rxflux.core.action.Action
import com.github.rougsig.rxflux.core.actor.Actor
import com.github.rougsig.rxflux.core.reducer.Reducer
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.subjects.PublishSubject

class Store : Observable<Action<*>>() {
  private val actionQueue = PublishSubject.create<Action<*>>()

  private val reducers = HashMap<String, Reducer<*>>()
  private val actors = HashMap<String, Actor>()

  init {
    @Suppress
    actionQueue
      .subscribe { action ->
        reducers.values
          .forEach { reducer -> reducer.accept(action) }

        actors.values
          .forEach { actor -> actor.accept(action) }
      }
  }

  fun dispatch(action: Action<*>) {
    actionQueue.onNext(action)
  }

  fun addReducer(reducer: Reducer<*>): Boolean {
    val isExists = reducers.containsKey(reducer.namespace)

    if (!isExists) {
      reducers[reducer.namespace] = reducer
    }

    return !isExists
  }

  fun removeReducer(reducer: Reducer<*>) {
    reducers.remove(reducer.namespace)
  }

  fun addActor(actor: Actor): Boolean {
    val isExists = reducers.containsKey(actor.namespace)

    if (!isExists) {
      actors[actor.namespace] = actor
    }

    actor.subscribe(actionQueue)

    return !isExists
  }

  fun removeActor(actor: Actor) {
    actors.remove(actor.namespace)
  }

  override fun subscribeActual(observer: Observer<in Action<*>>) {
    actionQueue.subscribe(observer)
  }
}