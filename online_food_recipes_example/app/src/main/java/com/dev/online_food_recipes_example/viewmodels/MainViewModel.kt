package com.dev.online_food_recipes_example.viewmodels

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.*
import com.dev.online_food_recipes_example.data.Repository
import com.dev.online_food_recipes_example.data.database.entities.RecipesEntity
import com.dev.online_food_recipes_example.models.FoodRecipe
import com.dev.online_food_recipes_example.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

// After Dagger-Hilt v2.31, @ViewModelInject has been removed by Google,
// use @HiltViewModel and @Inject instead
@HiltViewModel
class MainViewModel /*@ViewModelInject*/ @Inject constructor(
    application: Application,
    private val repository: Repository
) : AndroidViewModel(application) {

    /* ======================== Local ROOM Database ======================== */

    // Cast the Flow-Type into LiveData-Type
    val readRecipes: LiveData<List<RecipesEntity>> = repository.local.readDatabase().asLiveData()

    // This scope will be canceled when ViewModel will be cleared,
    // example: ViewModel.onCleared is called
    private fun insertRecipes(recipesEntity: RecipesEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.insertRecipes(recipesEntity)
        }
    }

    private fun offlineCacheRecipes(foodRecipesData: FoodRecipe) {
        val recipesEntity = RecipesEntity(foodRecipesData)
        insertRecipes(recipesEntity)
    }

    /* ======================== Remote Server ======================== */

    var recipesResponse: MutableLiveData<NetworkResult<FoodRecipe>> = MutableLiveData()

    fun getRecipes(queries: Map<String, String>) {
        // This scope will be canceled when ViewModel will be cleared,
        // example: ViewModel.onCleared is called
        viewModelScope.launch {
            getRecipesSafeCall(queries)
        }
    }

    private suspend fun getRecipesSafeCall(queries: Map<String, String>) {
        recipesResponse.value = NetworkResult.Loading()
        if (hasInternetConnection()) {
            try {
                val response = repository.remote.getRecipes(queries)
                recipesResponse.value = handleFoodRecipesResponse(response)

                // Save newest Data into Database
                // Use "!!" to ensure the Data is not null
                val foodRecipesData = recipesResponse.value!!.data
                if (foodRecipesData != null) {
                    offlineCacheRecipes(foodRecipesData)
                }

            } catch (exception: Exception) {
                recipesResponse.value = NetworkResult.Error(message = "Recipes not found.")
            }
        } else {
            recipesResponse.value = NetworkResult.Error(message = "No Internet Connection...")
        }
    }

    private fun handleFoodRecipesResponse(response: Response<FoodRecipe>): NetworkResult<FoodRecipe>? {
        when {
            /* ======================== Failure ======================== */
            // Request Timeout
            response.message().toString().contains("timeout") -> {
                return NetworkResult.Error(message = "Timeout")
            }

            // 402 -> Request reached limited
            response.code() == 402 -> {
                return NetworkResult.Error(message = "API Ket Limited.")
            }

            // Response Body is Null or Empty
            response.body()!!.results.isNullOrEmpty() -> {
                return NetworkResult.Error(message = "Recipes not found")
            }

            /* ======================== Success ======================== */
            response.isSuccessful -> {
                return NetworkResult.Success(response.body())
            }

            /* ======================== Else ======================== */
            else -> {
                return NetworkResult.Error(message = response.message())
            }
        }
    }


    // Check Network Status
    private fun hasInternetConnection(): Boolean {

        val connectivityManager = getApplication<Application>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager

        val activeNetwork = connectivityManager.activeNetwork ?: return false

        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false

        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true

            else -> false
        }
    }

}