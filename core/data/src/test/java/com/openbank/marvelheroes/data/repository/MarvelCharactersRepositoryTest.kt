package com.openbank.marvelheroes.data.repository

import android.graphics.Bitmap
import com.openbank.marvelheroes.data.datasource.MarvelCharactersRemoteDataSource
import com.openbank.marvelheroes.data.datasource.MarvelImageRemoteDataSource
import com.openbank.marvelheroes.marvelapi.response.CharacterObject
import com.openbank.marvelheroes.marvelapi.response.ResourceItem
import com.openbank.marvelheroes.marvelapi.response.ResourcesObject
import com.openbank.marvelheroes.marvelapi.response.ThumbnailObject
import com.openbank.marvelheroes.common.option.None
import com.openbank.marvelheroes.common.option.Value
import com.openbank.marvelheroes.data.datasource.CharactersDataSource
import io.mockk.MockKAnnotations
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import kotlin.random.Random

internal class MarvelCharactersRepositoryTest {

    private lateinit var marvelCharactersRepository: CharactersRepository

    @MockK private lateinit var mockkMarvelCharactersRemoteDataSource: MarvelCharactersRemoteDataSource
    @MockK private lateinit var mockkMarvelImageRemoteDataSource: MarvelImageRemoteDataSource

    @MockK private lateinit var mockkImage: Bitmap

    private val character1Id = Random(System.currentTimeMillis()).nextInt()

    private val mockkCharacterObject1: CharacterObject = CharacterObject(
        id = character1Id,
        name = "hero",
        description = "he is an hero",
        thumbnail = ThumbnailObject("https://path/to/image", "jpg"),
        comics = ResourcesObject(1, listOf(ResourceItem("https://path/to/comic", "comic"))),
        series = ResourcesObject(1, listOf(ResourceItem("https://path/to/serie", "serie"))),
        stories = ResourcesObject(1, listOf(ResourceItem("https://path/to/story", "story"))),
        events = ResourcesObject(1, listOf(ResourceItem("https://path/to/event", "event")))
    )

    private val mockkCharacterObject2: CharacterObject = CharacterObject(
        id = Random(System.currentTimeMillis()).nextInt(),
        name = "villain",
        description = "he is an villain",
        thumbnail = ThumbnailObject("https://path/to/image2", "jpg"),
        comics = ResourcesObject(1, listOf(ResourceItem("https://path/to/comic2", "comic2"))),
        series = ResourcesObject(1, listOf(ResourceItem("https://path/to/serie2", "serie2"))),
        stories = ResourcesObject(1, listOf(ResourceItem("https://path/to/story2", "story2"))),
        events = ResourcesObject(1, listOf(ResourceItem("https://path/to/event2", "event2")))
    )

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        marvelCharactersRepository = MarvelCharactersRepository(mockkMarvelCharactersRemoteDataSource,
            mockkMarvelImageRemoteDataSource)
    }

    @Test
    fun `get a new page of characters from online API`() {
        every { mockkMarvelCharactersRemoteDataSource.get(any(), any()) } returns CharactersDataSource.ListOutput(listOf(mockkCharacterObject1), 1, 0, 1)
        every { mockkMarvelImageRemoteDataSource.get(any()) } returns Value(mockkImage)

        val result = marvelCharactersRepository.getCharacters(1)

        assertEquals(1, result.list.size)
        assertEquals(mockkCharacterObject1.id, result.list[0].id)
        assertEquals(mockkCharacterObject1.name, result.list[0].name)
        assertEquals(mockkCharacterObject1.description, result.list[0].description)
        assertEquals(mockkImage, result.list[0].image)

        verify(exactly = 1) { mockkMarvelCharactersRemoteDataSource.get(any(), any()) }
        confirmVerified(mockkMarvelCharactersRemoteDataSource)
    }

    @Test
    fun `get a new page of characters from stored pages`() {
        every { mockkMarvelCharactersRemoteDataSource.get(any(), any(), any()) } returns CharactersDataSource.ListOutput(listOf(mockkCharacterObject2), 1, 0, 1)
        every { mockkMarvelImageRemoteDataSource.get(any()) } returns Value(mockkImage)

        // First time, charactersRemoteDataSource.get is called
        var result = marvelCharactersRepository.getCharacters(1)

        assertEquals(1, result.list.size)
        // Second time, charactersRemoteDataSource.get is not called and it is used the stored list
        result = marvelCharactersRepository.getCharacters(1)

        assertEquals(1, result.list.size)
        assertEquals(mockkCharacterObject2.id, result.list[0].id)
        assertEquals(mockkCharacterObject2.name, result.list[0].name)
        assertEquals(mockkCharacterObject2.description, result.list[0].description)
        assertEquals(mockkImage, result.list[0].image)

        verify(exactly = 1) { mockkMarvelCharactersRemoteDataSource.get(any(), any()) }
        confirmVerified(mockkMarvelCharactersRemoteDataSource)
    }

    @Test
    fun `get character detail`() {
        every { mockkMarvelCharactersRemoteDataSource.get(character1Id) } returns Value(mockkCharacterObject1)
        every { mockkMarvelImageRemoteDataSource.get(any()) } returns None

        val result = marvelCharactersRepository.getCharacterDetail(character1Id)

        assertTrue(result is Value)
        with ((result as Value).get) {
            assertEquals(mockkCharacterObject1.id, id)
            assertEquals(mockkCharacterObject1.name, name)
            assertEquals(mockkCharacterObject1.description, description)
            assertEquals(mockkCharacterObject1.comics.items[0].name, comics[0])
            assertEquals(mockkCharacterObject1.series.items[0].name, series[0])
            assertEquals(mockkCharacterObject1.stories.items[0].name, stories[0])
            assertEquals(mockkCharacterObject1.events.items[0].name, events[0])
            assertNull(image)
        }

        verify(exactly = 1) { mockkMarvelCharactersRemoteDataSource.get(character1Id) }
        confirmVerified(mockkMarvelCharactersRemoteDataSource)
    }

    @Test
    fun `get character fails`() {
        every { mockkMarvelCharactersRemoteDataSource.get(character1Id) } returns None

        val result = marvelCharactersRepository.getCharacterDetail(character1Id)

        assertTrue(result is None)

        verify(exactly = 1) { mockkMarvelCharactersRemoteDataSource.get(character1Id) }
        confirmVerified(mockkMarvelCharactersRemoteDataSource)
    }
}