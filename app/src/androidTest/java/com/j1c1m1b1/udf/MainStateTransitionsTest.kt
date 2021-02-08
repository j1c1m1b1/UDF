package com.j1c1m1b1.udf

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.j1c1m1b1.presentation.main.MainEvent
import com.j1c1m1b1.presentation.main.MainState
import kotlinx.coroutines.FlowPreview
import org.junit.Test
import org.junit.runner.RunWith

@FlowPreview
@RunWith(AndroidJUnit4::class)
internal class MainStateTransitionsTest {

    @Test
    fun whenSendSuccessIsSentWithInitialStateResultingStateIsSuccess() {
        val expectedMessage = "Hello world"
        val expectedCompleteMessage = "Message: $expectedMessage\n"
        val expectedFirstState = MainState.Loading
        val expectedFinalState = MainState.Complete(result = expectedCompleteMessage)

        MainEvent.SuccessRequest(message = expectedMessage).dispatchAndAssertStateTransitions(
            initialState = MainState.Initial, expectedFirstState, expectedFinalState
        )
    }
}