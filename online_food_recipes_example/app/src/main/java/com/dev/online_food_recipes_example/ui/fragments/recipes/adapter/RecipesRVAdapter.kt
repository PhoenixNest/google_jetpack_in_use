package com.dev.online_food_recipes_example.ui.fragments.recipes.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.dev.online_food_recipes_example.databinding.RvRecipesItemBinding
import com.dev.online_food_recipes_example.models.FoodRecipe
import com.dev.online_food_recipes_example.models.Result

class RecipesRVAdapter : RecyclerView.Adapter<RecipesRVAdapter.MyViewHolder>() {

    private var recipes = emptyList<Result>()

    fun setData(foodRecipeData: FoodRecipe) {

        // Calculate the list diff
        val recipesDiffUtil = RecipesDiffUtil(
            oldList = recipes,
            newList = foodRecipeData.results
        )

        val recipesDiffUtilResult = DiffUtil.calculateDiff(recipesDiffUtil)

        // Setup new DataList
        this.recipes = foodRecipeData.results

        // Binding the DiffUtil with RVAdapter
        recipesDiffUtilResult.dispatchUpdatesTo(this)
    }

    class MyViewHolder(private val binding: RvRecipesItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindData(result: Result) {
            binding.result = result

            // Observe the data change
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): MyViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = RvRecipesItemBinding.inflate(layoutInflater, parent, false)

                return MyViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentRecipes = recipes[position]
        holder.bindData(currentRecipes)
    }

    override fun getItemCount(): Int {
        return recipes.size
    }

}