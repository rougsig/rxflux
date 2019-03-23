package com.github.rougsig.rxflux.android.domain

data class LceState<out Content>(
  val isLoading: Boolean,
  val content: Content?,
  val error: String?
) {

  init {
    check(!(!isLoading && content == null && error == null)) {
      "LceState must always be in one of L,C or E states, got $this"
    }
  }

  companion object {
    fun <Content> Loading(content: Content? = null): LceState<Content> {
      return LceState(isLoading = true, content = content, error = null)
    }

    fun <Content> Content(content: Content): LceState<Content> {
      return LceState(isLoading = false, content = content, error = null)
    }

    fun <Content> Error(error: String, content: Content? = null): LceState<Content> {
      return LceState(isLoading = false, content = content, error = error)
    }
  }

  val isContent get() = !isLoading && error == null
  val isError get() = error != null

  fun asContent(): Content {
    check(!isLoading) { "expected content state, but isLoading=true" }
    check(error == null) { "expected content state, but error != null" }
    return content ?: throw IllegalStateException("expected content state, but content is null")
  }

  fun asError(): String {
    check(!isLoading) { "expected content state, but isLoading=true" }
    return error ?: throw IllegalStateException("expected error state, but error is null")
  }
}