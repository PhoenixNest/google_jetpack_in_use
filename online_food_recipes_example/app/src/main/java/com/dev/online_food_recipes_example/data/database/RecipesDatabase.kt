package com.dev.online_food_recipes_example.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.dev.online_food_recipes_example.data.database.entities.RecipesEntity

@Database(
    entities = [RecipesEntity::class],
    version = 1, // When you change the Database Schema, you need to increase this version number
    exportSchema = false // choose if save the History of Schema into Version Control Center
)
@TypeConverters(RecipesTypeConverter::class)
abstract class RecipesDatabase : RoomDatabase() {
    abstract fun recipesDao(): RecipesDao
}