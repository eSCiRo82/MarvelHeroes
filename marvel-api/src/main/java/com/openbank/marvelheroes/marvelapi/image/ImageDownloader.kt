package com.openbank.marvelheroes.marvelapi.image

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.openbank.marvelheroes.common.option.None
import com.openbank.marvelheroes.common.option.Option
import com.openbank.marvelheroes.common.option.Value
import retrofit2.http.Url
import java.io.IOException
import javax.inject.Inject

class ImageDownloader @Inject constructor(
    private val imageApiService: ImageApiService
) {

    private fun getEntryPoint(url: String) =
        if (url.startsWith("http"))
            url.substring(ImageApiService.baseUrl.length)
        else if (url.startsWith("https"))
            url.substring(ImageApiService.baseSecureUrl.length)
        else
            url

    fun downloadSync(@Url url: String) : Option<Bitmap> =
        ImageCache.get(url)?.let { image ->
            Value(image)
        } ?: run {
            try {
                imageApiService.downloadImage(getEntryPoint(url)).execute().let { response ->
                    if (response.isSuccessful) {
                        response.body()?.let { body ->
                            val bitmap = BitmapFactory.decodeStream(body.byteStream())
                            ImageCache.set(url, bitmap)
                            Value(bitmap)
                        }
                    } else None
                } ?: None
            } catch (e: IOException) {
                None
            }
        }

    fun downloadAsync(@Url url: String, loaded: (Bitmap) -> Unit) {
        ImageCache.get(url)?.let { image ->
            loaded(image)
        } ?: run {
            imageApiService.downloadImage(getEntryPoint(url))
                .enqueue(ImageDownloaderCallback(url, loaded))
        }
    }
}