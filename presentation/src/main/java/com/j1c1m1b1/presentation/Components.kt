package com.j1c1m1b1.presentation

import kotlinx.coroutines.flow.Flow

interface UiEvent<A : Action<S>, S : State> {
    suspend fun toAction(): Flow<A>
}

interface Action<S : State> {
    suspend fun perform(getPreviousState: () -> S): Flow<S>
}

interface State {
    interface Loading : State
    interface Complete<T : Any?> : State {
        val result: T
    }

    interface Error : State {
        val throwable: Throwable?
    }
}
