package com.openbank.marvelheroes.viewmodel

import androidx.lifecycle.ViewModel
import com.openbank.marvelheroes.common.extension.onFailure
import com.openbank.marvelheroes.common.extension.onSuccess
import com.openbank.marvelheroes.domain.usecase.GetCharactersUseCase
import com.openbank.marvelheroes.model.CharacterModel
import com.openbank.marvelheroes.viewmodel.extension.launchUseCase
import kotlinx.coroutines.flow.*
import javax.inject.Inject

/**
 * ViewModel associated to the characters gallery screen.
 */
class GalleryViewModel @Inject constructor(
    private val getCharactersUseCase: GetCharactersUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<State>(State.Idle)
    val state : StateFlow<State> = _state.asStateFlow()

    private val _event = MutableSharedFlow<Event>()
    val event : SharedFlow<Event> = _event.asSharedFlow()

    var currentPage = 0
    private set

    var isLastPage: Boolean = false
    private set

    private val currentLoadedCharacters: MutableList<CharacterModel> = mutableListOf()

    /**
     * Request a page of characters to the API. Each page contains 30 characters as maximum.
     */
    fun <T> getCharactersPage(page: Int = 1, map: (List<CharacterModel>) -> List<T>) = launchUseCase(
        useCase = getCharactersUseCase,
        input = page,
        onStart = { _state.value = State.Loading },
        onEach = { response ->
            response.onSuccess { result ->
                currentPage = page
                isLastPage = result.endPage
                currentLoadedCharacters.addAll(result.list)
                _state.value = State.CharactersPage(page,map(result.list))
            }
            .onFailure { _event.emit(Event.EmptyCharactersPage) }
        }
    )

    fun <T> loadCurrentCharacters(map: (List<CharacterModel>) -> List<T>) {
        _state.value = State.CharactersPage(currentPage, map(currentLoadedCharacters))
    }

    sealed class State {
        object Idle : State()
        object Loading : State()
        data class CharactersPage<T>(val page: Int, val list: List<T>) : State()
    }

    sealed class Event {
        object EmptyCharactersPage : Event()
    }
}