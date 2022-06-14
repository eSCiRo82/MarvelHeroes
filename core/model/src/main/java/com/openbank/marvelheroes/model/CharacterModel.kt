package com.openbank.marvelheroes.model

import android.graphics.Bitmap

data class CharacterModel(val id: Int,
                          val name: String,
                          val description: String,
                          val image: Bitmap?
)
