package com.github.rougsig.rxflux.android.domain.todolist

import com.github.rougsig.rxflux.android.core.LceState
import com.github.rougsig.rxflux.android.enitity.TodoItem

data class TodoListState(
  val todoListItems: LceState<List<TodoItem>>?,
  val addTodoItem: LceState<Unit>?,
  val removeTodoItem: LceState<Unit>?
)
