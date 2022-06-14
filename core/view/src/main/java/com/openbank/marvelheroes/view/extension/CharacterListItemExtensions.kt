package com.openbank.marvelheroes.view.extension

import com.openbank.marvelheroes.model.CharacterModel
import com.openbank.marvelheroes.view.list.character.CharacterItem

/** Conversion extensions from model to item */
fun CharacterModel.toItem() = CharacterItem(id, name, image)

fun List<CharacterModel>.toItem() = map { character -> character.toItem() }