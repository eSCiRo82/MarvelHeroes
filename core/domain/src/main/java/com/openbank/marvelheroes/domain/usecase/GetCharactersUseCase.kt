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
 * This use case make a request to the repository to obtain a page of characters. Each page contains
 * 30 characters as maximum.
 */
class GetCharactersUseCase @Inject constructor(
    private val repository: CharactersRepository
) : FlowUseCase<Int, Either<GetCharactersUseCase.Output, UseCaseError>>(Dispatchers.IO) {

    override fun launchFlow(input: Int): Flow<Either<Output, UseCaseError>> = flow {
        val result = repository.getCharacters(input)

        if (result.list.isEmpty()) emit(Either.Failure(NoCharactersFound))
        else emit(Either.Success(Output(result.list, result.endPage)))
    }

    data class Output(val list: List<CharacterModel>, val endPage: Boolean)
}