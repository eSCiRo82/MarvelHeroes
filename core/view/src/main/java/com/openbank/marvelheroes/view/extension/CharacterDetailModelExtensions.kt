package com.openbank.marvelheroes.view.extension

import com.openbank.marvelheroes.model.CharacterAppearanceModel
import com.openbank.marvelheroes.view.list.appearance.AppearanceItem

/** Conversion extensions from model to item */
fun CharacterAppearanceModel.toItem() = AppearanceItem(name, url)

fun List<CharacterAppearanceModel>.toItem() = map { appearance -> appearance.toItem() }