package com.openbank.marvelheroes.marvelapi

import com.openbank.marvelheroes.marvelapi.di.MarvelApiModule
import com.openbank.marvelheroes.marvelapi.extension.hash
import com.openbank.marvelheroes.marvelapi.request.ApiCallback
import com.openbank.marvelheroes.marvelapi.request.CharactersRequest
import com.openbank.marvelheroes.common.either.Either
import com.openbank.marvelheroes.common.extension.onFailure
import com.openbank.marvelheroes.common.extension.onSuccess
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.util.*

class MarvelApiServiceTest {

    private lateinit var service: MarvelApiService

    @Before
    fun setUp() {
        service = MarvelApiModule().provideMarvelApiService()
    }

    @Test
    fun `md5 digest to compute the hash for authentication`() {
        val params = mapOf<String, Any>(
            "ts" to 1L,
            "pvk" to "abcd",
            "pbk" to "1234"
        )

        val hash = params.hash()
        assertEquals("ffd275c5130566a2916217b101f26150", hash)
    }

    @Test
    fun `request marvel heroes synchronously`() {
        val request = CharactersRequest(service)
        val response = request.requestSync(mapOf<String, Any>(
            "ts" to Calendar.getInstance().timeInMillis,
            "limit" to 5
        ))

        assertTrue(response is Either.Success)
        response.onSuccess {
            assertEquals(200, it.code)
        }.onFailure { assertTrue(false) }
    }

    @Test
    fun `request character details synchronously`() {
        val request = CharactersRequest(service)
        val response = request.requestSync(mapOf<String, Any>(
            "id" to 1009150,
            "ts" to Calendar.getInstance().timeInMillis
        ))

        assertTrue(response is Either.Success)
        response.onSuccess {
            assertEquals(200, it.code)
            assertEquals(1, it.data.count)
            assertEquals(1009150, it.data.results[0].id)
            assertEquals("Agent Zero", it.data.results[0].name)
        }.onFailure { assertTrue(false) }
    }

    @Test
    fun `request marvel heroes asynchronously`() {
        val request = CharactersRequest(service)
        request.requestAsync(
            ApiCallback(
                onSuccess = { response ->
                    assertNotNull(response)
                    assertEquals(200, response?.code)
                },
                onFailure = { assertTrue(false) }
            ),
            mapOf<String, Any>(
                "ts" to Calendar.getInstance().timeInMillis
            )
        )
    }

    @Test
    fun `request character details asynchronously`() {
        val request = CharactersRequest(service)
        request.requestAsync(
            ApiCallback(
                onSuccess = { response ->
                    assertNotNull(response)
                    assertEquals(200, response?.code)
                    assertEquals(1, response?.data?.count)
                    assertEquals(1009150, response?.data?.results?.get(0)?.id)
                    assertEquals("Agent Zero", response?.data?.results?.get(0)?.name)
                },
                onFailure = { assertTrue(false) }
            ),
            mapOf<String, Any>(
                "id" to 1009150,
                "ts" to Calendar.getInstance().timeInMillis
            ))
    }
}