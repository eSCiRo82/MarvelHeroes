package com.openbank.marvelheroes.marvelapi.response

import com.google.gson.annotations.SerializedName
import retrofit2.http.Url

data class UrlObject(
    @SerializedName("type")
    val type: String,
    @SerializedName("url")
    @Url val url: String
)
