package com.openbank.marvelheroes.common.extension

import com.openbank.marvelheroes.common.option.None
import com.openbank.marvelheroes.common.option.Option
import com.openbank.marvelheroes.common.option.Value

fun <A, B> Option<A>.map(f: (A) -> B): Option<B> =
    when (this) {
        is Value -> Value(f(get))
        else -> None
    }

fun <A> Option<A>.getOrElse(default: () -> A): A =
    when (this) {
        is Value -> get
        else -> default()
    }