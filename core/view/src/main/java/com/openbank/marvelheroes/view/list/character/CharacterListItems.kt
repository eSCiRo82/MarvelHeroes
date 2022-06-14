package com.openbank.marvelheroes.view.list.character

import android.graphics.Bitmap

interface CharactersListItem

/** This item describes the content of a character card in the gallery */
data class CharacterItem(val id: Int, val name: String, val image: Bitmap?) : CharactersListItem

/** This item shows a card with a message in the gallery */
data class MessageItem(val message: String): CharactersListItem
