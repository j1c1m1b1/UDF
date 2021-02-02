package com.j1c1m1b1.presentation.main

import com.j1c1m1b1.presentation.State

sealed class MainState : State {
    object Loading : MainState(), State.Loading
    data class Complete(override val result: String) : MainState(), State.Complete<String>
    data class Error(override val throwable: Throwable? = null) : MainState(), State.Error
}
