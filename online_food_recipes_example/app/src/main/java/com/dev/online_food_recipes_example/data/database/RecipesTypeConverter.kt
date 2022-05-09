package com.dev.online_food_recipes_example.data.database

import androidx.room.TypeConverter
import com.dev.online_food_recipes_example.models.FoodRecipe
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class RecipesTypeConverter {

    private val gson = Gson()

    /*
     * Gson SourceCode:
     * public String toJson(Object src) {
     *      if (src == null) {
     *          return toJson(JsonNull.INSTANCE);
     *      }
     *   return toJson(src, src.getClass());
     * }
     */
    @TypeConverter
    fun foodRecipesToJsonString(recipe: FoodRecipe): String {
        return gson.toJson(recipe)
    }

    @TypeConverter
    fun jsonStringToFoodRecipes(json: String): FoodRecipe {
        // Get the FoodRecipes::Class type
        val type = object : TypeToken<FoodRecipe>() {}.type

        return gson.fromJson(json, type)
    }

}