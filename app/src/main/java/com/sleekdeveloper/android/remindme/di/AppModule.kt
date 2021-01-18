package com.sleekdeveloper.android.remindme.di

import android.content.Context
import androidx.room.Room
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.sleekdeveloper.android.remindme.data.source.AppDataSource
import com.sleekdeveloper.android.remindme.data.source.AppRepository
import com.sleekdeveloper.android.remindme.data.source.DefaultAppRepository
import com.sleekdeveloper.android.remindme.data.source.local.AppDatabase
import com.sleekdeveloper.android.remindme.data.source.local.LocalDataSource
import com.sleekdeveloper.android.remindme.data.source.remote.FirebaseDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
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

    @Singleton
    @Provides
    @RemoteAppDataSource
    fun provideRemoteDataSource(
        firestore: FirebaseFirestore,
        ioDispatcher: CoroutineDispatcher
    ): AppDataSource {
        return FirebaseDataSource(firestore, ioDispatcher)
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
    fun provideFirestore(): FirebaseFirestore = Firebase.firestore

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