package com.openbank.marvelheroes.marvelapi.extension

fun ByteArray.toHexString() = joinToString("") { String.format("%02x", it) }