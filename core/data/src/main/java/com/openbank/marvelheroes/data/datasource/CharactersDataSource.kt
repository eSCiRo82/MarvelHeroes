package com.openbank.marvelheroes.data.datasource

import com.openbank.marvelheroes.common.option.Option
import com.openbank.marvelheroes.marvelapi.MarvelApiService
import com.openbank.marvelheroes.marvelapi.response.CharacterObject

/**
 * Data source methods required.
 */
interface CharactersDataSource {

    fun get(id: Int): Option<CharacterObject>

    fun get(startsWith: String? = null,
            limit: Int = MarvelApiService.defaultLimit,
            offset: Int = MarvelApiService.defaultOffset) : ListOutput

    data class ListOutput(val list: List<CharacterObject>,
                          val total: Int,
                          val start: Int,
                          val count: Int)
}