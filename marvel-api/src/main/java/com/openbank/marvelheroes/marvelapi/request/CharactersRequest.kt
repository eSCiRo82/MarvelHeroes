package com.openbank.marvelheroes.marvelapi.request

import com.openbank.marvelheroes.marvelapi.BuildConfig
import com.openbank.marvelheroes.marvelapi.MarvelApiService
import com.openbank.marvelheroes.marvelapi.MarvelApiService.Companion.defaultLimit
import com.openbank.marvelheroes.marvelapi.MarvelApiService.Companion.defaultOffset
import com.openbank.marvelheroes.marvelapi.extension.exists
import com.openbank.marvelheroes.marvelapi.extension.hash
import com.openbank.marvelheroes.marvelapi.extension.param
import com.openbank.marvelheroes.marvelapi.response.ApiResponse
import com.openbank.marvelheroes.marvelapi.response.CharacterObject
import retrofit2.Call
import java.util.*
import javax.inject.Inject

class CharactersRequest @Inject constructor(
    private val marvelApiService: MarvelApiService
) : ApiRequest<CharacterObject>() {

    private val maxPerPage = 100
    private val minPerPage = 1

    override fun launchRequest(params: Map<String, Any>): Call<ApiResponse<CharacterObject>> =
        if (params.exists<Int>("id")) {
            marvelApiService.character(
                id = params["id"] as Int,
                apikey = params.param("pbk", BuildConfig.MARVEL_PBK),
                ts = params.param("ts", 1L),
                hash = params.hash()
            )
        } else {
            val startsWith = params.param<String?>("startsWith", null)
            val limit = params.param("limit", defaultLimit)

            marvelApiService.characters(
                startsWith = startsWith,
                limit = minOf(maxPerPage, maxOf(limit, minPerPage)),
                offset = params.param("offset", defaultOffset),
                apikey = params.param("pbk", BuildConfig.MARVEL_PBK),
                ts = params.param("ts", 1L),
                hash = params.hash()
            )
        }
}