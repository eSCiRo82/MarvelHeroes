package com.openbank.marvelheroes.domain.usecase

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
internal class GetCharactersByNameUseCaseTest {

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    private lateinit var getCharactersByNameUseCase: GetCharactersByNameUseCase

    @MockK private lateinit var repository: CharactersRepository

    private val character1 = CharacterModel(1, "character1", "description1", null)
    private val character2 = CharacterModel(2, "character2", "description2", null)
    private val character3 = CharacterModel(3, "character3", "description3", null)

    private val charactersList = listOf(character1, character2, character3)

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        getCharactersByNameUseCase = GetCharactersByNameUseCase(repository)
    }

    @Test
    fun `get the character detail is success`() = coroutineTestRule.runTest {
        coEvery { repository.getCharactersByName("char", 1) } returns CharactersRepository.CharactersOutput(charactersList, false)

        getCharactersByNameUseCase.prepare(GetCharactersByNameUseCase.Input("char", 1)).collect { result ->
            result.onSuccess { output ->
                assertEquals(charactersList, output.list)
                assertFalse(output.endPage)
            }.onFailure {
                assertTrue(false)
            }
        }

        coVerify(exactly = 1) { repository.getCharactersByName("char", 1) }
        confirmVerified(repository)
    }

    @Test
    fun `get the character detail fails`() = coroutineTestRule.runTest {
        coEvery { repository.getCharactersByName("char", 1) } returns CharactersRepository.CharactersOutput(
            listOf(), false)

        getCharactersByNameUseCase.prepare(GetCharactersByNameUseCase.Input("char", 1)).collect { result ->
            result.onFailure { fail ->
                assertTrue(fail is NoCharactersFound)
            }.onSuccess {
                assertTrue(false)
            }
        }

        coVerify(exactly = 1) { repository.getCharactersByName("char", 1) }
        confirmVerified(repository)
    }
}