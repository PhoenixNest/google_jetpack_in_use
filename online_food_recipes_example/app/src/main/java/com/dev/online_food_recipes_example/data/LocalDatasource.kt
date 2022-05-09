package com.dev.online_food_recipes_example.data

import com.dev.online_food_recipes_example.data.database.RecipesDao
import com.dev.online_food_recipes_example.data.database.entities.RecipesEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

// When use @Inject constructor(...),
// Hilt will auto-search which return value has match the constructor table and create an INSTANCE of it
class LocalDatasource @Inject constructor(
    private val recipesDao: RecipesDao
) {

    fun readDatabase(): Flow<List<RecipesEntity>> {
        return recipesDao.readRecipes()
    }

    // Use "suspend" keyword to let the system know this is an Kotlin coroutine function.
    suspend fun insertRecipes(recipesEntity: RecipesEntity) {
        recipesDao.insertData(recipesEntity)
    }
}