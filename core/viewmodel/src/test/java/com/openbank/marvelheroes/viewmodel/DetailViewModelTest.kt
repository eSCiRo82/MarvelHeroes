package com.openbank.marvelheroes.viewmodel

import app.cash.turbine.test
import com.openbank.marvelheroes.common.either.Either
import com.openbank.marvelheroes.domain.type.CharacterNotFound
import com.openbank.marvelheroes.domain.usecase.GetCharacterDetailUseCase
import com.openbank.marvelheroes.model.CharacterAppearanceModel
import com.openbank.marvelheroes.model.CharacterDetailModel
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
import kotlin.random.Random
import kotlin.time.ExperimentalTime

@ExperimentalCoroutinesApi
@ExperimentalTime
internal class DetailViewModelTest {

    @get:Rule val coroutineTestRule = CoroutineTestRule()

    private lateinit var detailViewModel: DetailViewModel

    @MockK private lateinit var mockkGetCharacterDetailUseCase: GetCharacterDetailUseCase

    private val characterDetail = CharacterDetailModel(
        id = Random(System.currentTimeMillis()).nextInt(),
        name = "villain",
        description = "he is an villain",
        image = null,
        comics = listOf(CharacterAppearanceModel("comic1", "http://comic1.url"),
            CharacterAppearanceModel("comic2", "http://comic2.url"),
            CharacterAppearanceModel("comic3", "http://comic3.url"),
            CharacterAppearanceModel("comic4", "http://comic4.url")),
        series = listOf(CharacterAppearanceModel("serie1", "http://serie1.url"),
            CharacterAppearanceModel("serie2", "http://serie2.url")),
        stories = listOf(CharacterAppearanceModel("story1", "http://story1.url"),
            CharacterAppearanceModel("story2", "http://story2.url"),
            CharacterAppearanceModel("story3", "http://story3.url")),
        events = listOf(CharacterAppearanceModel("event1", "http://event1.url")),
    )

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        detailViewModel = DetailViewModel(mockkGetCharacterDetailUseCase)
    }

    @Test
    fun `get selected character detail`() = coroutineTestRule.runTest {
        coEvery { mockkGetCharacterDetailUseCase.prepare(1) } answers { flow { emit(Either.Success(characterDetail)) } }

        detailViewModel.getCharacterDetail(1)

        detailViewModel.event.test {
            val item = awaitItem()
            assertTrue(item is DetailViewModel.Event.CharacterDetail)
            with (item as DetailViewModel.Event.CharacterDetail) {
                assertEquals(characterDetail, detail)
            }
            cancelAndIgnoreRemainingEvents()
        }

        coVerify(exactly = 1) { mockkGetCharacterDetailUseCase.prepare(1) }
        confirmVerified(mockkGetCharacterDetailUseCase)
    }

    @Test
    fun `character detail not found`() = coroutineTestRule.runTest {
        coEvery { mockkGetCharacterDetailUseCase.prepare(1) } answers { flow { emit(Either.Failure(CharacterNotFound)) } }

        detailViewModel.getCharacterDetail(1)

        detailViewModel.event.test {
            val item = awaitItem()
            assertTrue(item is DetailViewModel.Event.CharacterNotFound)
            cancelAndIgnoreRemainingEvents()
        }

        coVerify(exactly = 1) { mockkGetCharacterDetailUseCase.prepare(1) }
        confirmVerified(mockkGetCharacterDetailUseCase)
    }
}