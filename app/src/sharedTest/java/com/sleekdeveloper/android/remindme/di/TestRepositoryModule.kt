package com.sleekdeveloper.android.remindme.di

import com.sleekdeveloper.android.remindme.data.source.AppRepository
import com.sleekdeveloper.android.remindme.data.source.FakeTestRepository
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