package com.j1c1m1b1.udf.components

interface ErroneousOperationSimulator {

    fun getException(): Throwable {
        return SimulatedException("Random exception")
    }

    data class SimulatedException(override val message: String) : Throwable(message)
}