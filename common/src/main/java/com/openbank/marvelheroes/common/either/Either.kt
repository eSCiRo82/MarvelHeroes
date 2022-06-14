package com.openbank.marvelheroes.common.either

/**
 * Either object allow to encode two different results, depending on what it is needed to send. It
 * is usually used to represent success or failure result of an operation.
 */
sealed class Either<out S, out F>
{
    data class Success<out S>(val success: S): Either<S, Nothing>()
    data class Failure<out F>(val failure: F): Either<Nothing, F>()
}