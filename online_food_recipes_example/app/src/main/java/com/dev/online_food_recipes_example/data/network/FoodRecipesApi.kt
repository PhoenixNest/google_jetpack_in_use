package com.dev.online_food_recipes_example.data.network

import com.dev.online_food_recipes_example.models.FoodRecipe
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface FoodRecipesApi {

    @GET("/recipes/complexSearch")
    // Use "suspend" keyword to let the system know this is an Kotlin coroutine function.
    suspend fun getRecipes(
        @QueryMap queries: Map<String, String>
    ): Response<FoodRecipe>

}