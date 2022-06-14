package com.openbank.marvelheroes

import android.app.Application
import com.openbank.marvelheroes.di.ApplicationModule
import com.openbank.marvelheroes.common.di.ComponentProvider
import com.openbank.marvelheroes.common.di.DaggerComponent
import com.openbank.marvelheroes.di.ApplicationComponent
import com.openbank.marvelheroes.di.DaggerApplicationComponent
import com.openbank.marvelheroes.view.di.CoreViewSubcomponent
import javax.inject.Inject


class AndroidApplication @Inject constructor(): Application(), ComponentProvider {

    override val components: MutableMap<Class<*>, DaggerComponent> = mutableMapOf()

    private lateinit var applicationComponent: ApplicationComponent

    override fun onCreate() {
        super.onCreate()

        applicationComponent = DaggerApplicationComponent.builder()
            .applicationModule(ApplicationModule(this))
            .build()

        addSubcomponents()
    }

    private fun addSubcomponents() {
        applicationComponent.coreViewSubcomponent().create().apply {
            components[CoreViewSubcomponent::class.java] = applicationComponent.coreViewSubcomponent().create()
        }
    }
}