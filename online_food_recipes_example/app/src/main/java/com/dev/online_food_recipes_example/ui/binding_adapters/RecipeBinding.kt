package com.dev.online_food_recipes_example.ui.binding_adapters

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.dev.online_food_recipes_example.data.database.entities.RecipesEntity
import com.dev.online_food_recipes_example.models.FoodRecipe
import com.dev.online_food_recipes_example.utils.NetworkResult

class RecipeBinding {
    companion object {

        // Check Error ImageView
        @BindingAdapter(
            "android:readApiResponse",
            "android:readDatabase",
            requireAll = true
        )
        fun errorImageViewVisibility(
            imageView: ImageView,
            apiResponse: NetworkResult<FoodRecipe>,
            database: List<RecipesEntity>
        ) {
            if (apiResponse is NetworkResult.Error && database.isNullOrEmpty()) {
                imageView.visibility = View.VISIBLE
            } else if (apiResponse is NetworkResult.Loading) {
                imageView.visibility = View.INVISIBLE
            } else if (apiResponse is NetworkResult.Success) {
                imageView.visibility = View.INVISIBLE
            }
        }

        // Check Error TextView
        @BindingAdapter(
            "android:readApiResponse2",
            "android:readDatabase2",
            requireAll = true
        )
        fun errorTextViewVisibility(
            textView: TextView,
            apiResponse: NetworkResult<FoodRecipe>,
            database: List<RecipesEntity>
        ) {
            if (apiResponse is NetworkResult.Error && database.isNullOrEmpty()) {
                textView.visibility = View.VISIBLE
                textView.text = apiResponse.message.toString()
            } else if (apiResponse is NetworkResult.Loading) {
                textView.visibility = View.INVISIBLE
            } else if (apiResponse is NetworkResult.Success) {
                textView.visibility = View.INVISIBLE
            }
        }
    }
}