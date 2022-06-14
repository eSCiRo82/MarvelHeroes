package com.openbank.marvelheroes.domain.usecase

import app.cash.turbine.test
import com.openbank.marvelheroes.common.extension.onFailure
import com.openbank.marvelheroes.common.extension.onSuccess
import com.openbank.marvelheroes.data.repository.CharactersRepository
import com.openbank.marvelheroes.domain.type.NoCharactersFound
import com.openbank.marvelheroes.model.CharacterModel
import com.openbank.marvelheroes.test.CoroutineTestRule
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.time.ExperimentalTime

@ExperimentalCoroutinesApi
@ExperimentalTime
internal class GetCharactersUseCaseTest {

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    private lateinit var getCharactersUseCase: GetCharactersUseCase

    @MockK private lateinit var repository: CharactersRepository

    private val character1 = CharacterModel(1, "character1", "description1", null)
    private val character2 = CharacterModel(2, "character2", "description2", null)
    private val character3 = CharacterModel(3, "character3", "description3", null)

    private val charactersList = listOf(character1, character2, character3)

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        getCharactersUseCase = GetCharactersUseCase(repository)
    }

    @Test
    fun `get the character detail is success`() = coroutineTestRule.runTest {
        coEvery { repository.getCharacters(1) } returns CharactersRepository.CharactersOutput(charactersList, false)

        getCharactersUseCase.prepare(1).test {
            val result = awaitItem()
            result.onSuccess { output ->
                assertEquals(charactersList, output.list)
                assertFalse(output.endPage)
            }.onFailure {
                assertTrue(false)
            }
            cancelAndConsumeRemainingEvents()
        }

        coVerify(exactly = 1) { repository.getCharacters(1) }
        confirmVerified(repository)
    }

    @Test
    fun `get the character detail fails`() = coroutineTestRule.runTest {
        coEvery { repository.getCharacters(1) } returns CharactersRepository.CharactersOutput(
            listOf(), false)

        getCharactersUseCase.prepare(1).test {
            val result = awaitItem()
            result.onFailure { fail ->
                assertTrue(fail is NoCharactersFound)
            }.onSuccess {
                assertTrue(false)
            }
            cancelAndIgnoreRemainingEvents()
        }

        coVerify(exactly = 1) { repository.getCharacters(1) }
        confirmVerified(repository)
    }
}