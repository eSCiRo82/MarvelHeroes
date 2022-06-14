package com.openbank.marvelheroes.data.datasource

import android.graphics.Bitmap
import com.openbank.marvelheroes.common.option.Option

/**
 * Methods to be implemented by the data sources that request images.
 */
interface ImageDataSource {

    fun get(url: String) : Option<Bitmap>

    fun getAsync(url: String, loaded: (Bitmap) -> Unit)
}