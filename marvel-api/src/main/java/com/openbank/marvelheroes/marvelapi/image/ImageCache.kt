package com.openbank.marvelheroes.marvelapi.image

import android.graphics.Bitmap

/**
 * Cache to store downloaded images. It should be check before request an image, just in case it
 * was downloaded previously
 */
object ImageCache {

    private val cache: MutableMap<CharSequence, Bitmap> = mutableMapOf()

    fun set(key: CharSequence, image: Bitmap) = cache.put(key, image)

    fun get(key: CharSequence) : Bitmap? = cache[key]

    fun clear() = cache.clear()
}