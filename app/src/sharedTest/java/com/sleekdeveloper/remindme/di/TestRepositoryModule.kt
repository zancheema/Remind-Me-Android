package com.sleekdeveloper.remindme.di

import com.sleekdeveloper.remindme.data.source.AppRepository
import com.sleekdeveloper.remindme.data.source.FakeTestRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
abstract class TestRepositoryModule {
    @Singleton
    @Binds
    abstract fun bindRepository(repository: FakeTestRepository): AppRepository
}