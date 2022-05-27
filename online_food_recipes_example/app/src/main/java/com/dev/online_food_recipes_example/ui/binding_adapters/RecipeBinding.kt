package com.dev.online_food_recipes_example.ui.binding_adapters

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import com.dev.online_food_recipes_example.data.database.entities.RecipesEntity
import com.dev.online_food_recipes_example.models.FoodRecipe
import com.dev.online_food_recipes_example.utils.NetworkResult

class RecipeBinding {
    companion object {

        // Check Food Recipe Error Message
        @BindingAdapter(
            "android:readApiResponse",
            "android:readDatabase",
            requireAll = true
        )
        @JvmStatic
        fun handleReadDataError(
            view: View,
            apiResponse: NetworkResult<FoodRecipe>?,
            database: List<RecipesEntity>?
        ) {
            when (view) {
                is ImageView -> {
                    view.isVisible = apiResponse is NetworkResult.Error && database.isNullOrEmpty()
                }
                is TextView -> {
                    view.isVisible = apiResponse is NetworkResult.Error && database.isNullOrEmpty()
                    view.text = apiResponse?.message.toString()
                }
            }
        }
    }
}