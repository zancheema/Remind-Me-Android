package com.sleekdeveloper.android.remindme.di

import android.content.Context
import androidx.room.Room
import com.sleekdeveloper.android.remindme.data.source.AppDataSource
import com.sleekdeveloper.android.remindme.data.source.AppRepository
import com.sleekdeveloper.android.remindme.data.source.DefaultAppRepository
import com.sleekdeveloper.android.remindme.data.source.local.AppDatabase
import com.sleekdeveloper.android.remindme.data.source.local.LocalDataSource
import com.sleekdeveloper.android.remindme.data.source.remote.RemoteDataSource
import com.sleekdeveloper.android.remindme.data.source.remote.RestApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Qualifier
import javax.inject.Singleton
import kotlin.annotation.AnnotationRetention.RUNTIME

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {
    @Qualifier
    @Retention(RUNTIME)
    annotation class RemoteAppDataSource

    @Qualifier
    @Retention(RUNTIME)
    annotation class LocalAppDataSource

    @Qualifier
    @Retention(RUNTIME)
    annotation class BaseUrl

    @Singleton
    @Provides
    @RemoteAppDataSource
    fun provideRemoteDataSource(
        service: RestApiService,
        ioDispatcher: CoroutineDispatcher
    ): AppDataSource {
        return RemoteDataSource(service, ioDispatcher)
    }

    @Singleton
    @Provides
    @LocalAppDataSource
    fun provideLocalDataSource(
        database: AppDatabase,
        ioDispatcher: CoroutineDispatcher
    ): AppDataSource {
        return LocalDataSource(database, ioDispatcher)
    }

    @Singleton
    @Provides
    fun provideRestApiService(
        retrofit: Retrofit
    ): RestApiService {
        return retrofit.create(RestApiService::class.java)
    }

    @Singleton
    @Provides
    fun provideRetrofit(
        @BaseUrl baseUrl: String,
        converterFactory: Converter.Factory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(converterFactory)
            .build()
    }

    @Provides
    @Singleton
    fun retrofitConverterFactory(): Converter.Factory {
        return GsonConverterFactory.create()
    }

    @Provides
    @Singleton
    @BaseUrl
    fun provideBaseUrl() = "http://10.0.2.2:3000"

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "RemindMe.db"
        ).build()
    }

    @Singleton
    @Provides
    fun provideIoDispatcher(): CoroutineDispatcher {
        return Dispatchers.IO
    }
}

@Module
@InstallIn(ApplicationComponent::class)
object AppRepositoryModule {

    @Singleton
    @Provides
    fun provideAppRepository(
        @AppModule.RemoteAppDataSource remoteDataSource: AppDataSource,
        @AppModule.LocalAppDataSource localDataSource: AppDataSource,
        ioDispatcher: CoroutineDispatcher
    ): AppRepository {
        return DefaultAppRepository(
            remoteDataSource, localDataSource, ioDispatcher
        )
    }
}