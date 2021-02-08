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
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch

@FlowPreview
abstract class UniDirectionalFlowViewModel<E : UiEvent<A, S>, A : Action<S>, S : State> :
    ViewModel(), UiEventsProcessor<E, A, S> {

    protected abstract val initialState: S

    private val stateLiveData: MutableLiveData<S> = initializeStateLiveData()

    final override val getPreviousState: () -> S
        get() = { stateLiveData.requireValue() }

    final override val events: Channel<E> = Channel()

    init {
        startEventsProcessing()
    }

    private fun startEventsProcessing() {
        viewModelScope.launch {
            events.consumeAsStatesFlow {
                stateLiveData.value = it
            }
        }
    }

    protected fun E.dispatch() {
        viewModelScope.launch { dispatchEvent(this@dispatch) }
    }

    private fun initializeStateLiveData(): MutableLiveData<S> =
        MutableLiveData(initialState)

    private fun MutableLiveData<S>.requireValue() = requireNotNull(value) { "State is null!" }

    fun observe(owner: LifecycleOwner, observingBlock: (S?) -> Unit): Observer<S> =
        Observer<S>(observingBlock).also { stateLiveData.observe(owner, it) }
}
