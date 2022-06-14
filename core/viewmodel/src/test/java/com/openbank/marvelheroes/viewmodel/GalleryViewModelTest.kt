package com.openbank.marvelheroes.viewmodel

import app.cash.turbine.test
import com.openbank.marvelheroes.common.either.Either
import com.openbank.marvelheroes.domain.type.NoCharactersFound
import com.openbank.marvelheroes.domain.usecase.GetCharactersUseCase
import com.openbank.marvelheroes.model.CharacterModel
import com.openbank.marvelheroes.test.CoroutineTestRule
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.time.ExperimentalTime

@ExperimentalCoroutinesApi
@ExperimentalTime
internal class GalleryViewModelTest {

    @get:Rule val coroutineTestRule = CoroutineTestRule()

    private lateinit var galleryViewModel: GalleryViewModel

    @MockK private lateinit var mockkGetCharactersUseCase: GetCharactersUseCase

    private val character1 = CharacterModel(1, "character1", "description1", null)
    private val character2 = CharacterModel(2, "character2", "description2", null)
    private val character3 = CharacterModel(3, "character3", "description3", null)

    private val charactersList = listOf(character1, character2, character3)

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        galleryViewModel = GalleryViewModel(mockkGetCharactersUseCase)
    }

    @Test
    fun `get selected page characters list is success`() = coroutineTestRule.runTest {
        coEvery { mockkGetCharactersUseCase.prepare(1) } answers { flow { emit(Either.Success(GetCharactersUseCase.Output(charactersList, false))) } }

        galleryViewModel.getCharactersPage(1) { charactersList }

        galleryViewModel.state.test {
            var item = awaitItem()
            assertTrue(item is GalleryViewModel.State.Idle)
            item = awaitItem()
            assertTrue(item is GalleryViewModel.State.Loading)
            item = awaitItem()
            assertTrue(item is GalleryViewModel.State.CharactersPage<*>)
            with (item as GalleryViewModel.State.CharactersPage<*>) {
                assertEquals(charactersList.size, list.size)
            }
            cancelAndIgnoreRemainingEvents()
        }

        coVerify(exactly = 1) { mockkGetCharactersUseCase.prepare(1) }
        confirmVerified(mockkGetCharactersUseCase)
    }

    @Test
    fun `get selected page characters list is empty`() = coroutineTestRule.runTest {
        coEvery { mockkGetCharactersUseCase.prepare(1) } answers { flow { emit(Either.Failure(NoCharactersFound)) } }

        galleryViewModel.getCharactersPage(1) { charactersList }

        galleryViewModel.event.test {
            val item = awaitItem()
            assertTrue(item is GalleryViewModel.Event.EmptyCharactersPage)
            cancelAndIgnoreRemainingEvents()
        }

        coVerify(exactly = 1) { mockkGetCharactersUseCase.prepare(1) }
        confirmVerified(mockkGetCharactersUseCase)
    }
}