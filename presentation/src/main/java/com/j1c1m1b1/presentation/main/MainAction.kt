package com.j1c1m1b1.presentation.main

import com.j1c1m1b1.presentation.Action
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.random.Random

sealed class MainAction : Action<MainState> {
    protected val delayMillis: Long
        get() = Random.nextLong(3L, 10L)

    data class SendSuccess(private val message: String) : MainAction() {
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
            when (this) {
                null -> message
                else -> listOf(this, message).joinToString()
            }

        private companion object {
            const val MESSAGE_TEMPLATE = "Message: %d"
        }
    }

    object SendError : MainAction() {
        override suspend fun perform(oldState: MainState): Flow<MainState> = flow {
            IllegalArgumentException("Random exception").takeIf {
                Random.nextBoolean()
            }.let {
                MainState.Error(throwable = it)
            }.also { emit(it) }
        }
    }
}