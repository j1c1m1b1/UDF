package com.j1c1m1b1.presentation

import kotlinx.coroutines.flow.flowOf

fun <S : State, A : Action<S>> A.toFlow() = flowOf(
    this
)
