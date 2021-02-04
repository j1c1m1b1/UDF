package com.j1c1m1b1.udf

import com.j1c1m1b1.presentation.main.MainAction
import com.j1c1m1b1.presentation.main.MainEvent
import com.j1c1m1b1.presentation.main.MainState
import kotlinx.coroutines.FlowPreview

@FlowPreview
class MainActivityViewModel : UniDirectionalFlowViewModel<MainEvent, MainAction, MainState>() {

    override val initialState: MainState
        get() = MainState.Initial

    fun sendError() {
        MainEvent.ErrorRequest.dispatch()
    }

    fun sendSuccess(message: String) {
        MainEvent.SuccessRequest(message = message).dispatch()
    }
}