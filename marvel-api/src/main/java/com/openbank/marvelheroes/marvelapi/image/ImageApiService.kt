package com.openbank.marvelheroes.marvelapi.image

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url

interface ImageApiService {

    companion object {
        const val baseUrl = "http://i.annihil.us/u/prod/marvel/i/mg/"
        const val baseSecureUrl = "https://i.annihil.us/u/prod/marvel/i/mg/"
    }

    @GET
    fun downloadImage(@Url url: String): Call<ResponseBody>
}