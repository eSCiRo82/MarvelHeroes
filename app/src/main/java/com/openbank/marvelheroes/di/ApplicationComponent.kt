package com.openbank.marvelheroes.di

import com.openbank.marvelheroes.data.di.CoreDataModule
import com.openbank.marvelheroes.marvelapi.di.MarvelApiModule
import com.openbank.marvelheroes.view.di.CoreViewModule
import com.openbank.marvelheroes.view.di.CoreViewSubcomponent
import com.openbank.marvelheroes.viewmodel.di.CoreViewModelModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [
    ApplicationModule::class,
    MarvelApiModule::class,
    CoreDataModule::class,
    CoreViewModule::class,
    CoreViewModelModule::class
])
interface ApplicationComponent {

    fun coreViewSubcomponent(): CoreViewSubcomponent.Factory
}