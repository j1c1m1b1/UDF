package com.j1c1m1b1.udf

import androidx.lifecycle.MutableLiveData
import com.j1c1m1b1.presentation.main.MainAction
import com.j1c1m1b1.presentation.main.MainEvent
import com.j1c1m1b1.presentation.main.MainState
import kotlinx.coroutines.FlowPreview

@FlowPreview
class MainActivityViewModel : UniDirectionalFlowViewModel<MainEvent, MainAction, MainState>() {
    override val stateLiveData: MutableLiveData<MainState> = MutableLiveData()

    init {
        startEventsProcessing()
    }

    fun sendError() {
        MainEvent.SendError.also { dispatch(it) }
    }

    fun sendSuccess(message: String) {
        MainEvent.SendSuccess(message = MESSAGE_TEMPLATE.format(message)).also { dispatch(it) }
    }

    private companion object {
        const val MESSAGE_TEMPLATE = "Message: %d"
    }
}