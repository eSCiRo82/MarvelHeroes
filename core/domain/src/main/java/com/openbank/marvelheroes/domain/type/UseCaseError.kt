package com.openbank.marvelheroes.domain.type

sealed class UseCaseError

object NoCharactersFound : UseCaseError()

object CharacterNotFound: UseCaseError()
