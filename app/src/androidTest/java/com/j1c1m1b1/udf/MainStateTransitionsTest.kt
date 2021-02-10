package com.j1c1m1b1.udf

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.j1c1m1b1.udf.components.ErroneousOperationSimulator
import com.j1c1m1b1.udf.components.MainEvent
import com.j1c1m1b1.udf.components.MainState
import com.j1c1m1b1.udf.components.SuccessOperationSimulator
import kotlinx.coroutines.FlowPreview
import org.junit.Test
import org.junit.runner.RunWith

@FlowPreview
@RunWith(AndroidJUnit4::class)
internal class MainStateTransitionsTest {

    private val successOperationSimulator = object : SuccessOperationSimulator {
        fun getMessage(newMessage: String, previousMessage: String? = null): String =
            previousMessage.appendMessageAbove(newMessage)
    }

    private val erroneousOperationSimulator = object : ErroneousOperationSimulator {}

    @Test
    fun whenSendSuccessIsSentWithInitialStateResultingStateIsSuccess() {
        val eventMessage = "Hello world"
        val expectedCompleteMessage =
            successOperationSimulator.getMessage(eventMessage)
        val expectedFirstState = MainState.Loading
        val expectedFinalState = MainState.Complete(result = expectedCompleteMessage)

        MainEvent.SuccessRequest(message = eventMessage).dispatchAndAssertStateTransitions(
            initialState = MainState.Initial, expectedFirstState, expectedFinalState
        )
    }

    @Test
    fun whenSendErrorIsSentWithInitialStateResultingStateIsSuccess() {
        val expectedThrowable = erroneousOperationSimulator.getException()
        val expectedFirstState = MainState.Loading
        val expectedFinalState = MainState.Error(throwable = expectedThrowable)

        MainEvent.ErrorRequest.dispatchAndAssertStateTransitions(
            initialState = MainState.Initial, expectedFirstState, expectedFinalState
        )
    }

    @Test
    fun whenSendSuccessIsSentAfterPreviousCompleteBothMessagesAreAppended() {
        val previousMessage = "Message: first message 😀"
        val eventMessage = "second Message 💪"
        val expectedCompleteMessage =
            successOperationSimulator.getMessage(
                previousMessage = previousMessage,
                newMessage = eventMessage
            )
        val expectedFirstState = MainState.Loading
        val expectedFinalState = MainState.Complete(result = expectedCompleteMessage)

        MainEvent.SuccessRequest(message = eventMessage).dispatchAndAssertStateTransitions(
            initialState = MainState.Complete(result = previousMessage), expectedFirstState, expectedFinalState
        )
    }
}