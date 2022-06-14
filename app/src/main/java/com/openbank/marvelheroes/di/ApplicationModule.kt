package com.openbank.marvelheroes.di

import android.app.Application
import com.openbank.marvelheroes.AndroidApplication
import dagger.Module
import dagger.Provides

@Module
class ApplicationModule(private val application: AndroidApplication) {
    @Provides
    internal fun provideApplication(): Application = application
}