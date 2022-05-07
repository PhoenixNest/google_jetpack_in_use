package com.dev.online_food_recipes_example.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.dev.online_food_recipes_example.utils.Constants

class RecipesViewModel(application: Application) : AndroidViewModel(application) {

    // Setup Query Parameter
    fun setUpQueries(): HashMap<String, String> {
        val queries: HashMap<String, String> = HashMap()

        queries[Constants.QUERY_API_KEY] = Constants.API_KEY
        queries[Constants.QUERY_NUMBER] = "20" // number of Item each time
        queries[Constants.QUERY_TYPE] = "snack"
        queries[Constants.QUERY_DIET] = "vegan"
        queries[Constants.QUERY_ADD_RECIPE_INFORMATION] = "true"
        queries[Constants.QUERY_FILL_INGREDIENTS] = "true"

        return queries
    }

}