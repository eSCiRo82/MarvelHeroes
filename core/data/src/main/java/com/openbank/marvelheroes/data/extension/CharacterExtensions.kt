package com.openbank.marvelheroes.data.extension

import com.openbank.marvelheroes.data.datasource.MarvelImageRemoteDataSource
import com.openbank.marvelheroes.marvelapi.response.CharacterObject
import com.openbank.marvelheroes.model.CharacterDetailModel
import com.openbank.marvelheroes.model.CharacterModel
import com.openbank.marvelheroes.common.extension.getOrElse
import com.openbank.marvelheroes.data.datasource.ImageDataSource
import com.openbank.marvelheroes.model.CharacterAppearanceModel

const val miniatureImage = "portrait_medium"
const val image = "portrait_fantastic"

fun CharacterObject.toModel(marvelImageRemoteDataSource: ImageDataSource) = CharacterModel(
    id = id,
    name = name,
    description = description,
    image = marvelImageRemoteDataSource.get("${thumbnail.path}/$miniatureImage.${thumbnail.extension}").getOrElse { null })

fun List<CharacterObject>.toModel(marvelImageRemoteDataSource: ImageDataSource) =
    map { character -> character.toModel(marvelImageRemoteDataSource) }

fun CharacterObject.toDetailModel(marvelImageRemoteDataSource: ImageDataSource)
    = CharacterDetailModel(
        id = id,
        name = name,
        description = description,
        image = marvelImageRemoteDataSource.get("${thumbnail.path}/$image.${thumbnail.extension}").getOrElse { null },
        comics = comics.items.map { comic ->
            CharacterAppearanceModel(comic.name, comic.resourceUri)
        },
        series = series.items.map { serie ->
            CharacterAppearanceModel(serie.name, serie.resourceUri)
        },
        stories = stories.items.map { story ->
            CharacterAppearanceModel(story.name, story.resourceUri)
        },
        events = events.items.map { event ->
            CharacterAppearanceModel(event.name, event.resourceUri)
        }
    )