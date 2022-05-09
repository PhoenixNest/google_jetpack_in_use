package com.dev.online_food_recipes_example.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dev.online_food_recipes_example.data.database.entities.RecipesEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipesDao {

    // Database table will basically have our Newest Recipes in a Database every time we request a New Data from API
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertData(recipesEntity: RecipesEntity)

    @Query("select * from recipes_table order by id asc")
    fun readRecipes(): Flow<List<RecipesEntity>>

}