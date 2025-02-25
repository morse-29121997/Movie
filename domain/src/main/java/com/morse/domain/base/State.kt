package com.morse.domain.base

sealed class State <out T> {

    data object Loading : State<Nothing>()

    data class Success<out T>(val data: T) : State<T>()

    data object Empty : State<Nothing>()

    data class Error (
        val exceptionType: ExceptionType
    ) : State<Nothing>()

}