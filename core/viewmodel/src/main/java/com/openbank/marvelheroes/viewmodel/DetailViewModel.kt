package com.openbank.marvelheroes.viewmodel

import androidx.lifecycle.ViewModel
import com.openbank.marvelheroes.common.extension.onFailure
import com.openbank.marvelheroes.common.extension.onSuccess
import com.openbank.marvelheroes.domain.usecase.GetCharacterDetailUseCase
import com.openbank.marvelheroes.model.CharacterDetailModel
import com.openbank.marvelheroes.viewmodel.extension.launchUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject

/**
 * ViewModel associated to the character detail screen.
 */
class DetailViewModel @Inject constructor(
    private val getCharacterDetailUseCase: GetCharacterDetailUseCase
) : ViewModel() {

    private val _event = MutableSharedFlow<Event>()
    val event : SharedFlow<Event> = _event.asSharedFlow()

    /**
     * Request the detail of the given character using the corresponding use case
     */
    fun getCharacterDetail(characterId: Int) = launchUseCase(
        useCase = getCharacterDetailUseCase,
        input = characterId,
        onEach = { response ->
            response.onSuccess { character -> _event.emit(Event.CharacterDetail(character)) }
                .onFailure { _event.emit(Event.CharacterNotFound) }
        }
    )

    sealed class Event {
        data class CharacterDetail(val detail: CharacterDetailModel) : Event()
        object CharacterNotFound : Event()
    }
}