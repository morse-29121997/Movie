package com.morse.domain.base

import kotlinx.coroutines.flow.*

abstract class FlowUseCase<in IncomingParamterType, out OutComingResultType>() {

    suspend operator fun invoke(args: IncomingParamterType? = null): Flow<State<OutComingResultType>> =
        execute(args)
            .onStart { emit(State.Loading) }
            .onEmpty { emit(State.Empty) }
            .catch { e ->
                emit(
                    State.Error(
                        e.toExceptionType()
                    )
                )
            }

    protected abstract suspend fun execute(args: IncomingParamterType? = null): Flow<State<OutComingResultType>>

}


abstract class UseCase<in IncomingParamterType, out OutComingResultType>() {

    suspend operator fun invoke(args: IncomingParamterType? = null): OutComingResultType =
        execute(args)

    protected abstract suspend fun execute(args: IncomingParamterType? = null): OutComingResultType

}

