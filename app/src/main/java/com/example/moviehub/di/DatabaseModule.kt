package com.example.moviehub.di

import android.content.Context
import androidx.room.Room
import com.example.moviehub.data.local.MovieDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provdieDatabase(
        @ApplicationContext context: Context
    ): MovieDatabase {
        return Room.databaseBuilder(
            context,
            MovieDatabase::class.java,
            "movie_hub_db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideDatabaseDao(db: MovieDatabase) = db.dao
}