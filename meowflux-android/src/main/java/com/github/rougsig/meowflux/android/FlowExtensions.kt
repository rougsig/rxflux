package com.github.rougsig.meowflux.android

import android.view.View
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

fun <S : Any> Flow<S>.observe(view: View, init: StateDiff<S>.() -> Unit) {
  view.addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
    private var scope: CoroutineScope? = null
    private val diff = StateDiff<S>().also(init)
    override fun onViewAttachedToWindow(v: View?) {
      scope = MainScope().also { scope ->
        scope.launch {
          this@observe.collect {
            diff.update(it)
          }
        }
      }
    }

    override fun onViewDetachedFromWindow(v: View?) {
      scope?.cancel()
      scope = null
    }
  })
}
