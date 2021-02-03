package com.j1c1m1b1.presentation.main

import com.j1c1m1b1.presentation.Action
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.concurrent.TimeUnit
import kotlin.random.Random

sealed class MainAction : Action<MainState> {
    data class SendSuccess(private val message: String) : MainAction() {
        private val delayMillis: Long
            get() = loadingSecondsRange.random().let { TimeUnit.SECONDS.toMillis(it) }

        override suspend fun perform(oldState: MainState): Flow<MainState> = flow {
            emit(MainState.Loading)
            val previousMessage = oldState.let { it as? MainState.Complete }?.result
            delay(delayMillis)
            previousMessage.toNewMessage().let {
                MainState.Complete(result = it)
            }.also {
                emit(it)
            }
        }

        private fun String?.toNewMessage(): String =
            buildString {
                appendLine(MESSAGE_TEMPLATE.format(message))
                this@toNewMessage?.let { append(it) }
            }

        private companion object {
            const val MESSAGE_TEMPLATE = "Message: %s"
            val loadingSecondsRange: LongRange = 1L..5L
        }
    }

    object SendError : MainAction() {
        override suspend fun perform(oldState: MainState): Flow<MainState> = flow {
            IllegalArgumentException("Random exception").takeIf {
                Random.nextBoolean()
            }.let {
                MainState.Error(throwable = it)
            }.also {
                emit(it)
            }
        }
    }
}