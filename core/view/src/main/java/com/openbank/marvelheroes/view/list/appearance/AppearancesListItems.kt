package com.openbank.marvelheroes.view.list.appearance

interface AppearancesListItem

/** This item describes the content of a section in the list */
data class SectionItem(val title: String): AppearancesListItem

/** This item describes the content of an appearance in the list */
data class AppearanceItem(val name: String, val url: String): AppearancesListItem