package com.openbank.marvelheroes.common.option

/**
 * This object allows to encode always success results and hides possible non desired results that
 * can be represented by the Nothing type.
 */
sealed class Option<out A>

data class Value<out A>(val get: A) : Option<A>()

object None : Option<Nothing>()
