package com.openbank.marvelheroes.marvelapi.extension

import com.openbank.marvelheroes.marvelapi.BuildConfig
import java.security.MessageDigest

inline fun <reified T> Map<String, Any>.exists(p: String) =
    containsKey(p) && get(p) is T

inline fun <reified T> Map<String, Any>.param(p: String, default: T) =
    if (exists<T>(p)) get(p) as T
    else default

fun Map<String, Any>.hash(): String {
    val md5 = MessageDigest.getInstance("MD5")
    return md5.digest(
        "${param("ts", 1L)}${param("pvk", BuildConfig.MARVEL_PVK)}${param("pbk", BuildConfig.MARVEL_PBK)}".toByteArray()
    ).toHexString()
}