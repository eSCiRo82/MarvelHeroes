package com.openbank.marvelheroes.common.usecase

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

/**
 * Prepares the flow where performing an use case operation.
 */
abstract class FlowUseCase<I, O>(protected open val dispatcher: CoroutineDispatcher) {

    fun prepare(input: I) = launchFlow(input).flowOn(dispatcher)

    protected abstract fun launchFlow(input: I): Flow<O>
}