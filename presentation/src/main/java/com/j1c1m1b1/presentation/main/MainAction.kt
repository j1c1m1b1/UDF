package com.j1c1m1b1.presentation.main

import com.j1c1m1b1.presentation.Action
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.concurrent.TimeUnit

sealed class MainAction : Action<MainState> {
    protected val delayMillis: Long
        get() = loadingSecondsRange.random().let { TimeUnit.SECONDS.toMillis(it) }

    data class SendSuccess(private val message: String) : MainAction(), SuccessOperationSimulator {

        override suspend fun perform(getPreviousState: () -> MainState): Flow<MainState> = flow {
            emit(MainState.Loading)
            val newMessage = getPreviousState().getNewMessage()
            emit(MainState.Complete(result = newMessage))
        }

        /**
         * Simulates a time consuming asynchronous successful operation
         */
        private suspend fun MainState.getNewMessage(): String {
            delay(delayMillis)
            return this.let { it as? MainState.Complete }?.result.appendMessageAbove(message)
        }
    }

    object SendError : MainAction(), ErroneousOperationSimulator {
        override suspend fun perform(getPreviousState: () -> MainState): Flow<MainState> = flow {
            emit(MainState.Loading)
            // Simulates a time consuming asynchronous unsuccessful operation
            delay(delayMillis)
            val exception = getException()
            emit(MainState.Error(throwable = exception))
        }
    }

    private companion object {
        val loadingSecondsRange: LongRange = 1L..5L
    }
}