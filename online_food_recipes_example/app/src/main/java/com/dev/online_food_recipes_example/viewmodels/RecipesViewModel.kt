package com.dev.online_food_recipes_example.viewmodels

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.dev.online_food_recipes_example.data.DataStoreRepository
import com.dev.online_food_recipes_example.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipesViewModel @Inject constructor(
    application: Application,
    private val dataStoreRepository: DataStoreRepository
) : AndroidViewModel(application) {

    private var mealType = Constants.DEFAULT_MEAL_TYPE
    private var dietType = Constants.DEFAULT_DIET_TYPE

    // This value is use to check the network Status
    var networkStatus = false
    var backOnline = false

    // get the value from Datastore
    val readMealAndDietType = dataStoreRepository.readMealAndDietType
    val readBackOnline = dataStoreRepository.readBackOnline.asLiveData()

    /* ======================== Local Datastore ======================== */

    // Save Bottom Sheet Chip Data into Datastore
    fun saveMealAndDietType(
        mealType: String,
        mealTypeId: Int,
        dietType: String,
        dietTypeId: Int
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepository.saveMealAndDietType(
                mealType = mealType,
                mealTypeId = mealTypeId,
                dietType = dietType,
                dietTypeId = dietTypeId
            )
        }
    }

    // Save Network Data into Datastore
    private fun saveBackOnline(backOnline: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepository.saveBackOnline(backOnline)
        }
    }

    /* ======================== Remote Query Parameter ======================== */

    // Setup Query Parameter
    fun setUpQueries(): HashMap<String, String> {
        val queries: HashMap<String, String> = HashMap()

        // Use Bottom Sheet Chip Data to build Query Parameter
        viewModelScope.launch {
            readMealAndDietType.collect { value ->
                mealType = value.selectedMealType
                dietType = value.selectedDietType
            }
        }

        queries[Constants.QUERY_API_KEY] = Constants.API_KEY
        queries[Constants.QUERY_NUMBER] = Constants.DEFAULT_RECIPES_NUMBER
        queries[Constants.QUERY_TYPE] = mealType
        queries[Constants.QUERY_DIET] = dietType
        queries[Constants.QUERY_ADD_RECIPE_INFORMATION] = "true"
        queries[Constants.QUERY_FILL_INGREDIENTS] = "true"

        return queries
    }

    /* ======================== Check App Network Status ======================== */

    fun showNetworkStatus() {
        // When Network is unavailable
        if (!networkStatus) {
            // Show Error Message
            Toast.makeText(
                getApplication(),
                "No Internet Connection...",
                Toast.LENGTH_SHORT
            ).show()

            // Save BackOnline Value into Datastore
            saveBackOnline(true)

        } else if (networkStatus) {
            if (backOnline) {
                Toast.makeText(
                    getApplication(),
                    "We're back online...",
                    Toast.LENGTH_SHORT
                ).show()

                // Save BackOnline Value into Datastore
                saveBackOnline(false)
            }
        }
    }

}