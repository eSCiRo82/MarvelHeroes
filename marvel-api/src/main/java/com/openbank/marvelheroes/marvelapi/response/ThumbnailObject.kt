package com.openbank.marvelheroes.marvelapi.response

import com.google.gson.annotations.SerializedName
import retrofit2.http.Url

data class ThumbnailObject(
    @SerializedName("path")
    @Url val path: String,
    @SerializedName("extension")
    val extension: String
)