package com.openbank.marvelheroes.common.di

interface DaggerComponent

interface ComponentProvider {

    val components: MutableMap<Class<*>, DaggerComponent>
        get() = mutableMapOf()
}
