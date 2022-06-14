package com.openbank.marvelheroes.marvelapi.response

import com.google.gson.annotations.SerializedName
import retrofit2.http.Url

data class ResourceItem(
    @SerializedName("resourceURI")
    @Url val resourceUri: String,
    @SerializedName("name")
    val name: String
)
