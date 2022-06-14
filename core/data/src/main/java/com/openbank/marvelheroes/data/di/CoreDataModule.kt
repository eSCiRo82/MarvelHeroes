package com.openbank.marvelheroes.data.di

import com.openbank.marvelheroes.data.datasource.MarvelCharactersRemoteDataSource
import com.openbank.marvelheroes.data.datasource.CharactersDataSource
import com.openbank.marvelheroes.data.datasource.ImageDataSource
import com.openbank.marvelheroes.data.datasource.MarvelImageRemoteDataSource
import com.openbank.marvelheroes.data.repository.CharactersRepository
import com.openbank.marvelheroes.data.repository.MarvelCharactersRepository
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface CoreDataModule {

    @Binds
    @Singleton
    fun bindCharactersRemoteDataSource(impl: MarvelCharactersRemoteDataSource):
            CharactersDataSource

    @Binds
    @Singleton
    fun bindImageRemoteDataSource(impl: MarvelImageRemoteDataSource): ImageDataSource

    @Binds
    @Singleton
    fun bindMarvelCharactersRepository(impl: MarvelCharactersRepository): CharactersRepository
}