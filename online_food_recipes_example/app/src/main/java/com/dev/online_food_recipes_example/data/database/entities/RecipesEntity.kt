package com.dev.online_food_recipes_example.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.dev.online_food_recipes_example.models.FoodRecipe
import com.dev.online_food_recipes_example.utils.Constants

@Entity(tableName = Constants.RECIPES_TABLE)
class RecipesEntity(
    // Only one column which we're going to have in our Database
    var foodRecipe: FoodRecipe
) {
    @PrimaryKey(autoGenerate = false)
    var id: Int = 0
}