package com.j1c1m1b1.presentation.main

import com.j1c1m1b1.presentation.Action
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.concurrent.TimeUnit
import kotlin.random.Random

sealed class MainAction : Action<MainState> {
    protected val delayMillis: Long
        get() = loadingSecondsRange.random().let { TimeUnit.SECONDS.toMillis(it) }

    data class SendSuccess(private val message: String) : MainAction() {

        override suspend fun perform(oldState: MainState): Flow<MainState> = flow {
            emit(MainState.Loading)
            val newMessage = oldState.getNewMessage()
            emit(MainState.Complete(result = newMessage))
        }

        /**
         * Simulates a time consuming asynchronous successful operation
         */
        private suspend fun MainState.getNewMessage(): String {
            delay(delayMillis)
            return this.let { it as? MainState.Complete }?.result.toNewMessage()
        }

        private fun String?.toNewMessage(): String =
            buildString {
                appendLine(MESSAGE_TEMPLATE.format(message))
                this@toNewMessage?.let { append(it) }
            }

        private companion object {
            const val MESSAGE_TEMPLATE = "Message: %s"
        }
    }

    object SendError : MainAction() {
        override suspend fun perform(oldState: MainState): Flow<MainState> = flow {
            emit(MainState.Loading)
            val exception = getException()
            emit(MainState.Error(throwable = exception))

        }

        /**
         * Simulates a time consuming asynchronous unsuccessful operation
         */
        private suspend fun getException(): Throwable? {
            delay(delayMillis)
            return IllegalArgumentException("Random exception").takeIf {
                Random.nextBoolean()
            }
        }
    }

    private companion object {
        val loadingSecondsRange: LongRange = 1L..5L
    }
}