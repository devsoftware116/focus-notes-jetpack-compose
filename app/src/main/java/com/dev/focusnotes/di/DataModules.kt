package com.dev.focusnotes.di

import kotlin.jvm.java
import android.content.Context
import androidx.room.Room
import com.dev.focusnotes.data.repository.DefaultNotesRepository

import com.dev.focusnotes.data.repository.NotesRepository
import com.dev.focusnotes.data.source.local.NoteDao

import com.dev.focusnotes.data.source.local.NotesDatabase
import com.dev.focusnotes.data.source.network.NetworkDataSource
import com.dev.focusnotes.data.source.network.NotesApiService
import com.dev.focusnotes.data.source.network.RetrofitNetworkDataSource
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

// Repository Binding
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Singleton
    @Binds
    abstract fun bindNotesRepository(
        repository: DefaultNotesRepository
    ): NotesRepository
}

// Remote Data Source Binding
@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {

}

// Room Database + DAO
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): NotesDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            NotesDatabase::class.java,
            "Notes.db"
        ).build()
    }

    @Provides
    fun provideNotesDao(database: NotesDatabase): NoteDao = database.notesDao()
}

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    fun provideBaseUrl() = "http://10.0.2.2:8080/"

    @Singleton
    @Provides
    fun provideRetrofit(baseUrl: String): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    fun provideNotesApiService(retrofit: Retrofit): NotesApiService {
        return retrofit.create(NotesApiService::class.java)
    }

    @Singleton
    @Provides
    fun provideNetworkDataSource(apiService: NotesApiService): NetworkDataSource {
        return RetrofitNetworkDataSource(apiService)
    }
}
