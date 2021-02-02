package com.j1c1m1b1.presentation

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.launch

@FlowPreview
interface UiEventDispatcher<E : UiEvent<A, S>, A : Action<S>, S : State> {
    fun Flow<A>.toState(): StateFlow<S>
    val events: Channel<E>
    val scope: CoroutineScope

    suspend fun Channel<E>.startDataCollection(collectHandler: (state: S) -> (Unit)) {
        this.consumeAsFlow()
            .toAction()
            .toState()
            .collect { collectHandler(it) }
    }

    fun dispatch(event: E) {
        scope.launch { events.send(event) }
    }

    private fun Flow<E>.toAction(): Flow<A> = this.flatMapMerge { event ->
        event.toAction()
    }
}