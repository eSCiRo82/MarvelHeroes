package com.openbank.marvelheroes.common.extension

import com.openbank.marvelheroes.common.either.Either

inline fun <T, F> Either<T, F>.onSuccess(block: ((T) -> Unit)): Either<T, F> {
    if (this is Either.Success)
        block(this.success)
    return this
}

inline fun <R, T, F> Either<T, F>.mapSuccess(block: ((T) -> R)) : R? {
    if (this is Either.Success)
        return block(this.success)
    return null
}

inline fun <T, F> Either<T, F>.onFailure(block: ((F) -> Unit)): Either<T, F> {
    if (this is Either.Failure)
        block(this.failure)
    return this
}

inline fun <R, T, F> Either<T, F>.mapFailure(block: ((F) -> R)) : R? {
    if (this is Either.Failure)
        block(this.failure)
    return null
}