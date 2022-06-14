package com.openbank.marvelheroes.viewmodel

import androidx.lifecycle.ViewModel
import com.openbank.marvelheroes.model.CharacterModel
import com.openbank.marvelheroes.viewmodel.extension.launchUseCase
import com.openbank.marvelheroes.common.extension.onFailure
import com.openbank.marvelheroes.common.extension.onSuccess
import com.openbank.marvelheroes.domain.usecase.GetCharactersByNameUseCase
import kotlinx.coroutines.flow.*
import javax.inject.Inject

/**
 * ViewModel associated to the search character screen.
 */
open class SearchViewModel @Inject constructor(
    private val getCharactersByNameUseCase: GetCharactersByNameUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<State>(State.Idle)
    val state : StateFlow<State> = _state.asStateFlow()

    private val _event = MutableSharedFlow<Event>()
    val event : SharedFlow<Event> = _event.asSharedFlow()

    var currentPage = 0
    private set

    var isLastPage: Boolean = false
    private set

    /**
     * Request a search of characters using some initial characters of their name. The result can
     * be paginated if more than 30 characters are found.
     */
    fun <T> getCharactersByName(name: String, page: Int = 1, map: (List<CharacterModel>) -> List<T>) = launchUseCase(
        useCase = getCharactersByNameUseCase,
        input = GetCharactersByNameUseCase.Input(name, page),
        onStart = { _state.value = State.Loading },
        onEach = { response ->
            response.onSuccess { result ->
                currentPage = page
                isLastPage = result.endPage
                _state.value = State.CharactersPage(page,map(result.list))
            }
            .onFailure {
                _state.value = State.Idle
                _event.emit(Event.EmptyCharactersPage)
            }
        }
    )

    sealed class State {
        object Idle : State()
        object Loading : State()
        data class CharactersPage<T>(val page: Int, val list: List<T>) : State()
    }

    sealed class Event {
        object EmptyCharactersPage : Event()
    }
}