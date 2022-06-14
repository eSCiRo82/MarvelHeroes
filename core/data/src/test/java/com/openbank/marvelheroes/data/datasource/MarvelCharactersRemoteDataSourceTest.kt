package com.openbank.marvelheroes.data.datasource

import com.openbank.marvelheroes.marvelapi.request.CharactersRequest
import com.openbank.marvelheroes.marvelapi.response.ApiResponse
import com.openbank.marvelheroes.marvelapi.response.CharacterObject
import com.openbank.marvelheroes.marvelapi.type.ApiResponseError
import com.openbank.marvelheroes.common.either.Either
import com.openbank.marvelheroes.common.option.None
import com.openbank.marvelheroes.common.option.Value
import io.mockk.*
import io.mockk.impl.annotations.MockK
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import kotlin.random.Random

internal class MarvelCharactersRemoteDataSourceTest {

    private lateinit var marvelCharactersRemoteDataSource: MarvelCharactersRemoteDataSource

    @MockK private lateinit var mockkCharactersRequest: CharactersRequest

    private val character1Id = Random(System.currentTimeMillis()).nextInt()

    private val mockkCharacterObject1: CharacterObject = mockk {
        every { id } returns character1Id
        every { name } returns "hero"
    }

    private val mockkCharacterObject2: CharacterObject = mockk {
        every { id } returns Random(System.currentTimeMillis()).nextInt()
        every { name } returns "villain"
    }

    private val mockkCharacterObject3: CharacterObject = mockk {
        every { id } returns Random(System.currentTimeMillis()).nextInt()
        every { name } returns "partner"
    }

    private val mockkApiResponseSingleCharacter: ApiResponse<CharacterObject> = mockk {
        every { data.count } returns 1
        every { data.results } returns listOf(mockkCharacterObject1)
    }

    private val mockkApiResponseCharacterList: ApiResponse<CharacterObject> = mockk {
        every { data.total } returns 100
        every { data.count } returns 3
        every { data.offset } returns 0
        every { data.results } returns listOf(mockkCharacterObject1,
            mockkCharacterObject2,
            mockkCharacterObject3)
    }

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        marvelCharactersRemoteDataSource = MarvelCharactersRemoteDataSource(mockkCharactersRequest)
    }

    @Test
    fun `get character using his ID is success, then Option Value with character is returned`() {
        val params = mapOf("id" to character1Id)
        every { mockkCharactersRequest.requestSync(params) } returns Either.Success(mockkApiResponseSingleCharacter)

        val result = marvelCharactersRemoteDataSource.get(character1Id)

        assertTrue(result is Value)
        with (result as Value<CharacterObject>) {
            assertEquals(mockkCharacterObject1.id, get.id)
            assertEquals(mockkCharacterObject1.name, get.name)
        }

        verify(exactly = 1) { mockkCharactersRequest.requestSync(params) }
        confirmVerified(mockkCharactersRequest)
    }

    @Test
    fun `get character using his ID fails, then Option None is returned`() {
        every { mockkCharactersRequest.requestSync(any()) } returns Either.Failure(ApiResponseError.ApiResponseEmpty)

        val result = marvelCharactersRemoteDataSource.get(10001)

        assertTrue(result is None)

        verify(exactly = 1) { mockkCharactersRequest.requestSync(any()) }
        confirmVerified(mockkCharactersRequest)
    }

    @Test
    fun `get characters list is success, then a list of character objects is returned`() {
        every { mockkCharactersRequest.requestSync(any()) } returns Either.Success(mockkApiResponseCharacterList)

        val result = marvelCharactersRemoteDataSource.get(null, 20, 0)

        assertEquals(mockkApiResponseCharacterList.data.count, result.list.size)

        verify(exactly = 1) { mockkCharactersRequest.requestSync(any()) }
        confirmVerified(mockkCharactersRequest)
    }

    @Test
    fun `get characters list fails, then empty list is returned`() {
        every { mockkCharactersRequest.requestSync(any()) } returns Either.Failure(ApiResponseError.ApiResponseEmpty)

        val result = marvelCharactersRemoteDataSource.get(null, 20, 0)

        assertTrue(result.list.isEmpty())

        verify(exactly = 1) { mockkCharactersRequest.requestSync(any()) }
        confirmVerified(mockkCharactersRequest)
    }
}