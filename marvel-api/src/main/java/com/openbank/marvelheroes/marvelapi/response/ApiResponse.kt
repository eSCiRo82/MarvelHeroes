package com.openbank.marvelheroes.marvelapi.response

import com.google.gson.annotations.SerializedName

data class ApiResponse<T>(
    @SerializedName("code")
    val code: Int,
    @SerializedName("status")
    val status: String,
    @SerializedName("etag")
    val etag: String,
    @SerializedName("data")
    val data: DataObject<T>
)
