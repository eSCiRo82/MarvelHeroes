package com.openbank.marvelheroes.view.di

import com.openbank.marvelheroes.common.di.DaggerComponent
import com.openbank.marvelheroes.view.detail.DetailFragment
import com.openbank.marvelheroes.view.gallery.GalleryFragment
import com.openbank.marvelheroes.view.search.SearchFragment
import dagger.Subcomponent

@Subcomponent
interface CoreViewSubcomponent : DaggerComponent {

    @Subcomponent.Factory
    interface Factory {

        fun create(): CoreViewSubcomponent
    }

    fun inject(fragment: GalleryFragment)
    fun inject(fragment: DetailFragment)
    fun inject(fragment: SearchFragment)
}