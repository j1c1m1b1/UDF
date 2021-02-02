package com.j1c1m1b1.udf

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.j1c1m1b1.presentation.Action
import com.j1c1m1b1.presentation.State
import com.j1c1m1b1.presentation.UiEvent
import com.j1c1m1b1.presentation.UiEventsProcessor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flattenMerge
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@FlowPreview
abstract class UniDirectionalFlowViewModel<E : UiEvent<A, S>, A : Action<S>, S : State> :
    ViewModel(), UiEventsProcessor<E, A, S> {

    protected abstract val stateLiveData: MutableLiveData<S>

    final override val scope: CoroutineScope = viewModelScope

    final override val events: Channel<E> = Channel()

    protected open val initialEvent: E? = null

    init {
        startEventsProcessing()
    }

    private fun startEventsProcessing() {
        scope.launch {
            events.consumeAsStatesFlow {
                stateLiveData.value = it
            }
        }

        initialEvent?.let { dispatch(it) }
    }

    fun getCurrentState(): S? = stateLiveData.value

    override fun Flow<A>.toState(): Flow<S> =
        this.map { action ->
            action.perform(requireNotNull(stateLiveData.value))
        }.flattenMerge()

    fun observe(owner: LifecycleOwner, observingBlock: (S?) -> Unit): Observer<S> =
        Observer<S>(observingBlock).also { stateLiveData.observe(owner, it) }
}