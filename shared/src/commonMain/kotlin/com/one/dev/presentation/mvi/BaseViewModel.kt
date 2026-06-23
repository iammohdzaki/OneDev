package com.one.dev.presentation.mvi

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

// ── MVI interfaces ─────────────────────────────────────────────────────────────────────
interface UiState
interface UiEvent
interface UiEffect

// ── Base ViewModel ────────────────────────────────────────────────────────────────────
// Pure commonMain — zero Android/JVM dependencies.
abstract class BaseViewModel<S : UiState, E : UiEvent, F : UiEffect> {

    val viewModelScope: CoroutineScope =
        CoroutineScope(Dispatchers.Main + SupervisorJob())

    // — State ──────────────────────────────────────────────────────────────────
    private val _state = MutableStateFlow(createInitialState())
    val state: StateFlow<S> = _state.asStateFlow()

    protected abstract fun createInitialState(): S

    protected fun setState(reduce: S.() -> S) {
        _state.value = _state.value.reduce()
    }

    // — Events ─────────────────────────────────────────────────────────────────
    abstract fun onEvent(event: E)

    // — Effects (one-shot) ───────────────────────────────────────────────────
    private val _effect = Channel<F>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    protected fun sendEffect(builder: () -> F) {
        viewModelScope.launch { _effect.send(builder()) }
    }
}
