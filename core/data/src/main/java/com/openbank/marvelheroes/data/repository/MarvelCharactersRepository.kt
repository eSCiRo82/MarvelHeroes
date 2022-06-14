package com.openbank.marvelheroes.data.repository

import com.openbank.marvelheroes.data.datasource.CharactersDataSource
import com.openbank.marvelheroes.data.extension.toDetailModel
import com.openbank.marvelheroes.data.extension.toModel
import com.openbank.marvelheroes.model.CharacterDetailModel
import com.openbank.marvelheroes.common.extension.map
import com.openbank.marvelheroes.common.option.Option
import com.openbank.marvelheroes.common.option.Value
import com.openbank.marvelheroes.data.datasource.ImageDataSource
import javax.inject.Inject

/**
 * Implementation of the repository for Marvel API
 */
class MarvelCharactersRepository @Inject constructor(
    private val charactersRemoteDataSource: CharactersDataSource,
    private val marvelImageRemoteDataSource: ImageDataSource
) : CharactersRepository {

    private val pages: MutableMap<Int, CharactersRepository.CharactersOutput> = mutableMapOf()
    private val details: MutableList<CharacterDetailModel> = mutableListOf()

    override val charactersPerPage: Int
        get() = 30

    override fun getCharacters(page: Int): CharactersRepository.CharactersOutput =
        pages[page] ?: run {
            val charactersPage = charactersRemoteDataSource.get(null, charactersPerPage,
                if (page > 0) charactersPerPage * (page-1) else 0)
            val characters = charactersPage.list.toModel(marvelImageRemoteDataSource)

            val output = CharactersRepository.CharactersOutput(characters,
                charactersPage.start+charactersPage.count >= charactersPage.total)

            if (characters.isNotEmpty())
                pages[page] = output
            output
        }

    override fun getCharactersByName(name: String?, page: Int): CharactersRepository.CharactersOutput {
            val charactersPage = charactersRemoteDataSource.get(name, charactersPerPage,
                if (page > 0) charactersPerPage * (page-1) else 0)
            val characters = charactersPage.list.toModel(marvelImageRemoteDataSource)

            return CharactersRepository.CharactersOutput(characters,
                charactersPage.start+charactersPage.count >= charactersPage.total)
    }

    override fun getCharacterDetail(id: Int): Option<CharacterDetailModel> =
        details.find { detail -> detail.id == id }?.let { Value(it) }
            ?: run {
                charactersRemoteDataSource.get(id).map { it.toDetailModel(marvelImageRemoteDataSource) }
                    .apply {
                        if (this is Value)
                            details.add(get)
                    }
            }
}