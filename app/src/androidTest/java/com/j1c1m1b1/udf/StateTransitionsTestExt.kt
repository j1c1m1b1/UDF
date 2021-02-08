package com.j1c1m1b1.udf

import com.j1c1m1b1.presentation.Action
import com.j1c1m1b1.presentation.State
import com.j1c1m1b1.presentation.UiEvent
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectIndexed
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals

@FlowPreview
internal inline fun <reified S : State, A : Action<S>, E : UiEvent<A, S>> E.dispatchAndAssertStateTransitions(
    initialState: S,
    vararg expectedStates: S
) {
    runBlocking {
        toAction()
            .flatMapConcat { it.perform { initialState } }
            .collectIndexed { index, actualState ->
                assertEquals(expectedStates.getOrNull(index), actualState)
            }
    }
}
