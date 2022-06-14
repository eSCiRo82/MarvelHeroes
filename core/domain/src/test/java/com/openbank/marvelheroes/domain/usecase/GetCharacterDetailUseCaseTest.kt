package com.openbank.marvelheroes.domain.usecase

import app.cash.turbine.test
import com.openbank.marvelheroes.common.extension.onFailure
import com.openbank.marvelheroes.common.extension.onSuccess
import com.openbank.marvelheroes.common.option.None
import com.openbank.marvelheroes.common.option.Value
import com.openbank.marvelheroes.data.repository.CharactersRepository
import com.openbank.marvelheroes.domain.type.CharacterNotFound
import com.openbank.marvelheroes.model.CharacterDetailModel
import com.openbank.marvelheroes.test.CoroutineTestRule
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.time.ExperimentalTime

@ExperimentalCoroutinesApi
@ExperimentalTime
internal class GetCharacterDetailUseCaseTest {

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    private lateinit var getCharacterDetailUseCase: GetCharacterDetailUseCase

    @MockK private lateinit var repository: CharactersRepository

    private val characterDetail = CharacterDetailModel(
        id = 1,
        name = "character1",
        description = "description1",
        image = null,
        comics = listOf(),
        series = listOf(),
        stories = listOf(),
        events = listOf()
    )

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        getCharacterDetailUseCase = GetCharacterDetailUseCase(repository)
    }

    @Test
    fun `get the character detail is success`() = coroutineTestRule.runTest {
        coEvery { repository.getCharacterDetail(1) } returns Value(characterDetail)

        getCharacterDetailUseCase.prepare(1).collect { result ->
            result.onSuccess { detail ->
                assertEquals(characterDetail, detail)
            }
        }

        coVerify(exactly = 1) { repository.getCharacterDetail(1) }
        confirmVerified(repository)
    }

    @Test
    fun `get the character detail fails`() = coroutineTestRule.runTest {
        coEvery { repository.getCharacterDetail(1) } returns None

        getCharacterDetailUseCase.prepare(1).collect { result ->
            result.onFailure { fail ->
                assertTrue(fail is CharacterNotFound)
            }
        }

        coVerify(exactly = 1) { repository.getCharacterDetail(1) }
        confirmVerified(repository)
    }
}