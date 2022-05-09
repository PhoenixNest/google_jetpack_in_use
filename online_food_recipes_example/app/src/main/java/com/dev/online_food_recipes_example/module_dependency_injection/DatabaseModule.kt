package com.dev.online_food_recipes_example.module_dependency_injection

import android.content.Context
import androidx.room.Room
import com.dev.online_food_recipes_example.data.database.RecipesDao
import com.dev.online_food_recipes_example.data.database.RecipesDatabase
import com.dev.online_food_recipes_example.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

// All those are binding inside the DatabaseModule will be available in an application component
@Module
// After Dagger-Hilt v2.31, ApplicationComponent::class has been removed by Google, use SingletonComponent::class instead
@InstallIn(/*ApplicationComponent::class*/ SingletonComponent::class)
object DatabaseModule {

    // Use Singleton annotation to tell Hilt that we just need only one INSTANCE
    @Singleton
    // Because of we are use ROOM library,
    // which is an external library which not create by us
    @Provides
    fun provideDatabase(
        @ApplicationContext
        context: Context
    ): RecipesDatabase {
        return Room.databaseBuilder(
            context,
            RecipesDatabase::class.java,
            Constants.DATABASE_NAME
        ).build()
    }

    @Singleton
    @Provides
    fun provideRecipesDao(database: RecipesDatabase): RecipesDao {
        return database.recipesDao()
    }

}