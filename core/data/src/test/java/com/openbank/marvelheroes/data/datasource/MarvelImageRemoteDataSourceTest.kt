package com.openbank.marvelheroes.data.datasource

import android.graphics.Bitmap
import com.openbank.marvelheroes.marvelapi.image.ImageDownloader
import com.openbank.marvelheroes.common.option.None
import com.openbank.marvelheroes.common.option.Value
import io.mockk.MockKAnnotations
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

internal class MarvelImageRemoteDataSourceTest {

    private lateinit var marvelImageRemoteDataSource: MarvelImageRemoteDataSource

    @MockK private lateinit var mockkImageDownloader: ImageDownloader

    @MockK private lateinit var mockkImage: Bitmap

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        marvelImageRemoteDataSource = MarvelImageRemoteDataSource(mockkImageDownloader)
    }

    @Test
    fun `get image synchronously is success`() {
        every { mockkImageDownloader.downloadSync("http://request/image/url") } returns Value(mockkImage)

        val result = marvelImageRemoteDataSource.get("http://request/image/url")

        assertTrue(result is Value)
        assertEquals(mockkImage, (result as Value).get)

        verify(exactly = 1) { mockkImageDownloader.downloadSync("http://request/image/url") }
        confirmVerified(mockkImageDownloader)
    }

    @Test
    fun `get image synchronously fails`() {
        every { mockkImageDownloader.downloadSync("http://request/image/url") } returns None

        val result = marvelImageRemoteDataSource.get("http://request/image/url")

        assertTrue(result is None)

        verify(exactly = 1) { mockkImageDownloader.downloadSync("http://request/image/url") }
        confirmVerified(mockkImageDownloader)
    }

    @Test
    fun `get image asynchronously is success`() {
        every { mockkImageDownloader.downloadAsync("ttp://request/image/url", any()) } returns Unit

        marvelImageRemoteDataSource.getAsync("ttp://request/image/url") { }

        verify(exactly = 1) {
            mockkImageDownloader.downloadAsync("ttp://request/image/url", any())
        }
        confirmVerified(mockkImageDownloader)
    }
}