package com.openbank.marvelheroes.viewmodel.di

import androidx.lifecycle.ViewModel
import com.openbank.marvelheroes.viewmodel.DetailViewModel
import com.openbank.marvelheroes.viewmodel.GalleryViewModel
import com.openbank.marvelheroes.viewmodel.SearchViewModel
import com.openbank.marvelheroes.viewmodel.factory.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface CoreViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(GalleryViewModel::class)
    fun bindGalleryViewModel(impl: GalleryViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DetailViewModel::class)
    fun bindDetailViewModel(impl: DetailViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SearchViewModel::class)
    fun bindSearchViewModel(impl: SearchViewModel): ViewModel
}