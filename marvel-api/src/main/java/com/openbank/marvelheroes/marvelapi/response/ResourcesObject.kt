package com.openbank.marvelheroes.marvelapi.response

import com.google.gson.annotations.SerializedName

data class ResourcesObject(
    @SerializedName("available")
    val available: Int,
    @SerializedName("items")
    val items: List<ResourceItem>
)
