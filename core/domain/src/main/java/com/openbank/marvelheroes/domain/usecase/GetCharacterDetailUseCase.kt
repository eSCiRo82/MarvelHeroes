package com.openbank.marvelheroes.domain.usecase

import com.openbank.marvelheroes.data.repository.CharactersRepository
import com.openbank.marvelheroes.domain.type.CharacterNotFound
import com.openbank.marvelheroes.domain.type.UseCaseError
import com.openbank.marvelheroes.model.CharacterDetailModel
import com.openbank.marvelheroes.common.either.Either
import com.openbank.marvelheroes.common.option.Value
import com.openbank.marvelheroes.common.usecase.FlowUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * This use case make a request to the repository to obtain the detail of the given character.
 */
class GetCharacterDetailUseCase @Inject constructor(
    private val repository: CharactersRepository
) : FlowUseCase<Int, Either<CharacterDetailModel, UseCaseError>>(Dispatchers.IO) {

    override fun launchFlow(input: Int): Flow<Either<CharacterDetailModel, UseCaseError>> = flow {
        (repository.getCharacterDetail(input)).apply {
            if (this is Value) emit(Either.Success(get))
            else emit(Either.Failure(CharacterNotFound))
        }
    }
}