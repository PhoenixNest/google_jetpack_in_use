package com.dev.online_food_recipes_example.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.dev.online_food_recipes_example.utils.Constants
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

// Scope annotation for bindings that should exist for the life of an activity, surviving configuration.
@ActivityRetainedScoped
private val Context.dataStore by preferencesDataStore(Constants.PREFERENCE_NAME)

class DataStoreRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private object PreferenceKeys {
        val selectedMealType = stringPreferencesKey(Constants.PREFERENCE_MEAL_TYPE)
        val selectedMealTypeId = intPreferencesKey(Constants.PREFERENCE_MEAL_TYPE_ID)
        val selectedDietType = stringPreferencesKey(Constants.PREFERENCE_DIET_TYPE)
        val selectedDietTypeId = intPreferencesKey(Constants.PREFERENCE_DIET_TYPE_ID)
    }

    // Like the old style SharePreference, you should obtain the DataStore by context
    private val dataStore: DataStore<Preferences> = context.dataStore

    suspend fun saveMealAndDietType(
        mealType: String,
        mealTypeId: Int,
        dietType: String,
        dietTypeId: Int
    ) {
        // use .edit to 'Update' the Key-Value in DataStore
        dataStore.edit { preference ->
            preference[PreferenceKeys.selectedMealType] = mealType
            preference[PreferenceKeys.selectedMealTypeId] = mealTypeId
            preference[PreferenceKeys.selectedDietType] = dietType
            preference[PreferenceKeys.selectedDietTypeId] = dietTypeId
        }
    }

    val readMealAndDietType: Flow<MealAndDietType> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            // Convert the Datastore Value to Map
            val selectedMealType = preferences[PreferenceKeys.selectedMealType] ?: Constants.DEFAULT_MEAL_TYPE
            val selectedMealTypeId = preferences[PreferenceKeys.selectedMealTypeId] ?: 0
            val selectedDietType = preferences[PreferenceKeys.selectedDietType] ?: Constants.DEFAULT_DIET_TYPE
            val selectedDietTypeId = preferences[PreferenceKeys.selectedDietTypeId] ?: 0

            MealAndDietType(
                selectedMealType,
                selectedMealTypeId,
                selectedDietType,
                selectedDietTypeId
            )
        }
}

data class MealAndDietType(
    val selectedMealType: String,
    val selectedMealTypeId: Int,
    val selectedDietType: String,
    val selectedDietTypeId: Int
)