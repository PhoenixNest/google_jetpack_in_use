package com.dev.retrofit_in_use.module

import android.content.Context
import androidx.room.Room
import com.dev.retrofit_in_use.data.local.PixabayDao
import com.dev.retrofit_in_use.data.local.PixabayDatabase
import com.dev.retrofit_in_use.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Singleton
    @Provides
    fun provideDatabase(
        @ApplicationContext
        context: Context
    ): PixabayDatabase {
        return Room.databaseBuilder(
            context,
            PixabayDatabase::class.java,
            Constants.DATABASE_NAME,
        ).build()
    }

    @Singleton
    @Provides
    fun provideDao(database: PixabayDatabase): PixabayDao {
        return database.pixabayDao()
    }
}