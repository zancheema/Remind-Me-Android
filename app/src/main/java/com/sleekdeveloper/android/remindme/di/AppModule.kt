package com.sleekdeveloper.android.remindme.di

import com.sleekdeveloper.android.remindme.data.source.AppRepository
import com.sleekdeveloper.android.remindme.data.source.StubRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppRepositoryModule {

    @Singleton
    @Provides
    fun provideAppRepository(): AppRepository = StubRepository()
}