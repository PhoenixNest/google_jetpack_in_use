package com.dev.online_food_recipes_example.data

import com.dev.online_food_recipes_example.data.network.FoodRecipesApi
import com.dev.online_food_recipes_example.models.FoodRecipe
import retrofit2.Response
import javax.inject.Inject

// When use @Inject constructor(...),
// Hilt will auto-search which return value has match the constructor table and create an INSTANCE of it
class RemoteDatasource @Inject constructor(
    private val foodRecipesApi: FoodRecipesApi
) {

    // Use "suspend" keyword to let the system know this is an Kotlin coroutine function.
    suspend fun getRecipes(queries: Map<String, String>): Response<FoodRecipe> {
        return foodRecipesApi.getRecipes(queries)
    }
}