package com.j1c1m1b1.presentation.main

import com.j1c1m1b1.presentation.UiEvent
import com.j1c1m1b1.presentation.toFlow
import kotlinx.coroutines.flow.Flow

sealed class MainEvent : UiEvent<MainAction, MainState> {
    data class SendSuccess(private val message: String) : MainEvent() {
        override suspend fun toAction(): Flow<MainAction> =
            MainAction.SendSuccess(message = message).toFlow()
    }

    object SendError : MainEvent() {
        override suspend fun toAction(): Flow<MainAction> = MainAction.SendError.toFlow()
    }
}