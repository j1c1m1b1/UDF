package com.j1c1m1b1.presentation.main

interface SuccessOperationSimulator {

    fun String?.appendMessageAbove(message: String): String {
        return buildString {
            appendLine(MESSAGE_TEMPLATE.format(message))
            this@appendMessageAbove?.let { append(it) }
        }
    }

    private companion object {
        const val MESSAGE_TEMPLATE = "Message: %s"
    }
}