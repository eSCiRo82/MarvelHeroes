package com.openbank.marvelheroes.marvelapi

import com.openbank.marvelheroes.marvelapi.response.ApiResponse
import com.openbank.marvelheroes.marvelapi.response.CharacterObject
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface MarvelApiService {

    companion object {
        const val baseUrl = "https://gateway.marvel.com/"
        val defaultLimit: Int
            get() = 20

        val defaultOffset: Int
            get() = 0
    }

    @GET("/v1/public/characters")
    fun characters(@Query("nameStartsWith") startsWith: String?,
                   @Query("limit") limit: Int = defaultLimit,
                   @Query("offset") offset: Int = defaultOffset,
                   @Query("apikey") apikey: String,
                   @Query("ts") ts: Long,
                   @Query("hash") hash: String) : Call<ApiResponse<CharacterObject>>

    @GET("/v1/public/characters/{id}")
    fun character(@Path("id") id: Int,
                  @Query("apikey") apikey: String,
                  @Query("ts") ts: Long,
                  @Query("hash") hash: String) : Call<ApiResponse<CharacterObject>>
}