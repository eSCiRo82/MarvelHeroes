package com.openbank.marvelheroes.marvelapi.response

import com.google.gson.annotations.SerializedName

data class CharacterObject(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("thumbnail")
    val thumbnail: ThumbnailObject,
    @SerializedName("comics")
    val comics: ResourcesObject,
    @SerializedName("series")
    val series: ResourcesObject,
    @SerializedName("stories")
    val stories: ResourcesObject,
    @SerializedName("events")
    val events: ResourcesObject
)
