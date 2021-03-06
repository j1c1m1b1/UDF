package com.j1c1m1b1.presentation

import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapMerge

@FlowPreview
interface UiEventsProcessor<E : UiEvent<A, S>, A : Action<S>, S : State> {

    val getPreviousState: () -> S
    val events: Channel<E>

    suspend fun Channel<E>.consumeAsStatesFlow(collectHandler: (state: S) -> (Unit)) {
        this.consumeAsFlow()
            .toAction()
            .toState()
            .distinctUntilChanged()
            .collect { collectHandler(it) }
    }

    suspend fun dispatchEvent(event: E) {
        events.send(event)
    }

    private fun Flow<E>.toAction(): Flow<A> = this.flatMapMerge { event ->
        event.toAction()
    }

    private fun Flow<A>.toState(): Flow<S> = this.flatMapMerge { action ->
        action.perform(getPreviousState)
    }
}