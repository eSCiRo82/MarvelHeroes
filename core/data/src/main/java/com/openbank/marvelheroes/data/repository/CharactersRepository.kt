package com.openbank.marvelheroes.data.repository

import com.openbank.marvelheroes.marvelapi.MarvelApiService.Companion.defaultLimit
import com.openbank.marvelheroes.model.CharacterDetailModel
import com.openbank.marvelheroes.model.CharacterModel
import com.openbank.marvelheroes.common.option.Option

/**
 * Methods of the repository that will be required by the uses cases in domain
 */
interface CharactersRepository {

    val charactersPerPage : Int
        get() = defaultLimit

    fun getCharacters(page: Int): CharactersOutput

    fun getCharactersByName(name: String?, page: Int): CharactersOutput

    fun getCharacterDetail(id: Int): Option<CharacterDetailModel>

    data class CharactersOutput(val list: List<CharacterModel>, val endPage: Boolean = false)
}