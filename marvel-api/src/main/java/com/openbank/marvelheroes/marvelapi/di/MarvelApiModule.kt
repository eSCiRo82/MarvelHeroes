package com.openbank.marvelheroes.marvelapi.di

import com.openbank.marvelheroes.marvelapi.MarvelApiService
import com.openbank.marvelheroes.marvelapi.image.ImageApiService
import com.openbank.marvelheroes.marvelapi.request.ApiRequest
import com.openbank.marvelheroes.marvelapi.request.CharactersRequest
import com.openbank.marvelheroes.marvelapi.response.CharacterObject
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class MarvelApiModule {

    @Provides
    @Singleton
    fun provideMarvelApiService() : MarvelApiService =
        Retrofit.Builder()
            .baseUrl(MarvelApiService.baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MarvelApiService::class.java)

    @Provides
    @Singleton
    fun provideMarvelImageApiService(): ImageApiService =
        Retrofit.Builder()
            .baseUrl(ImageApiService.baseSecureUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ImageApiService::class.java)

    @Provides
    @Singleton
    fun provideCharactersRequest(request: CharactersRequest): ApiRequest<CharacterObject> = request
}