package com.j1c1m1b1.presentation

import kotlinx.coroutines.flow.flowOf

fun <S: State, A: Action<S>> A.toFlow() = flowOf(this)

inline fun <S : State, reified T : S> Action<S>.requireState(state: S) =
    require(state is T) { "Cannot perform action ${this::class.simpleName} on state $state" }