package com.openbank.marvelheroes.data.datasource

import android.graphics.Bitmap
import com.openbank.marvelheroes.marvelapi.image.ImageDownloader
import com.openbank.marvelheroes.common.option.Option
import javax.inject.Inject

/**
 * Implementation of the data source required methods to request an image to the remote API.
 */
class MarvelImageRemoteDataSource @Inject constructor(
    private val imageDownloader: ImageDownloader
) : ImageDataSource {

    override fun get(url: String): Option<Bitmap> = imageDownloader.downloadSync(url)

    override fun getAsync(url: String, loaded: (Bitmap) -> Unit) =
        imageDownloader.downloadAsync(url, loaded)
}