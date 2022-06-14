package com.openbank.marvelheroes.domain.usecase

import com.openbank.marvelheroes.data.repository.CharactersRepository
import com.openbank.marvelheroes.domain.type.NoCharactersFound
import com.openbank.marvelheroes.domain.type.UseCaseError
import com.openbank.marvelheroes.model.CharacterModel
import com.openbank.marvelheroes.common.either.Either
import com.openbank.marvelheroes.common.usecase.FlowUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * This use case make a request to the repository to obtain a list of characters whose name starts
 * with the string in the input.
 */
class GetCharactersByNameUseCase @Inject constructor(
    private val repository: CharactersRepository
) : FlowUseCase<GetCharactersByNameUseCase.Input, Either<GetCharactersByNameUseCase.Output, UseCaseError>>(Dispatchers.IO) {

    override fun launchFlow(input: Input): Flow<Either<Output, UseCaseError>> = flow {
        val result = repository.getCharactersByName(input.name, input.page)

        if (result.list.isEmpty()) emit(Either.Failure(NoCharactersFound))
        else emit(Either.Success(Output(result.list, result.endPage)))
    }

    data class Input(val name: String, val page: Int)
    data class Output(val list: List<CharacterModel>, val endPage: Boolean)
}