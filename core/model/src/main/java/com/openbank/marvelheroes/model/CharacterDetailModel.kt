package com.openbank.marvelheroes.model

import android.graphics.Bitmap

data class CharacterDetailModel(val id: Int,
                                val name: String,
                                val description: String,
                                val image: Bitmap?,
                                val comics: List<CharacterAppearanceModel>,
                                val series: List<CharacterAppearanceModel>,
                                val stories: List<CharacterAppearanceModel>,
                                val events: List<CharacterAppearanceModel>)
