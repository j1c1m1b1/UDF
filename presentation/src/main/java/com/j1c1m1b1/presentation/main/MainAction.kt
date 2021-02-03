package com.j1c1m1b1.presentation.main

import com.j1c1m1b1.presentation.Action
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.concurrent.TimeUnit
import kotlin.random.Random

sealed class MainAction : Action<MainState> {
    protected val delayMillis: Long
        get() = Random.nextLong(1L, 5L).let { TimeUnit.SECONDS.toMillis(it) }

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

        private fun String?.toNewMessage(): String {
            val newMessage = MESSAGE_TEMPLATE.format(message)
            return when (this) {
                null -> newMessage
                else -> listOf(this, newMessage).joinToString()
            }
        }

        private companion object {
            const val MESSAGE_TEMPLATE = "Message: %s"
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