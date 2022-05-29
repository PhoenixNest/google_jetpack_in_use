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

// Store Meal and Diet Type form Bottom Sheet into Datastore preference
data class MealAndDietType(
    val selectedMealType: String,
    val selectedMealTypeId: Int,
    val selectedDietType: String,
    val selectedDietTypeId: Int
)

class DataStoreRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private object PreferenceKeys {
        val selectedMealType = stringPreferencesKey(Constants.PREFERENCE_MEAL_TYPE)
        val selectedMealTypeId = intPreferencesKey(Constants.PREFERENCE_MEAL_TYPE_ID)
        val selectedDietType = stringPreferencesKey(Constants.PREFERENCE_DIET_TYPE)
        val selectedDietTypeId = intPreferencesKey(Constants.PREFERENCE_DIET_TYPE_ID)

        val backOnline = booleanPreferencesKey(Constants.PREFERENCE_BACK_ONLINE)
    }

    // Like the old style SharePreference, you should obtain the DataStore by context
    private val dataStore: DataStore<Preferences> = context.dataStore

    /* ============== Save Data into Datastore preference ============== */

    // Save Bottom Sheet Data into Datastore preference
    suspend fun saveMealAndDietType(
        mealType: String,
        mealTypeId: Int,
        dietType: String,
        dietTypeId: Int
    ) {
        // use .edit to 'Update' the Key-Value in DataStore
        dataStore.edit { dataStorePreference ->
            dataStorePreference[PreferenceKeys.selectedMealType] = mealType
            dataStorePreference[PreferenceKeys.selectedMealTypeId] = mealTypeId
            dataStorePreference[PreferenceKeys.selectedDietType] = dietType
            dataStorePreference[PreferenceKeys.selectedDietTypeId] = dietTypeId
        }
    }

    // Save Network Data into Datastore preference
    suspend fun saveBackOnline(backOnline: Boolean) {
        dataStore.edit { dataStorePreference ->
            dataStorePreference[PreferenceKeys.backOnline] = backOnline
        }
    }

    /* ===== Read Data from Datastore and trans it as Flow type to return the value ==== */

    val readMealAndDietType: Flow<MealAndDietType> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { dataStorePreference ->
            // Convert the Datastore Value to Map
            val selectedMealType = dataStorePreference[PreferenceKeys.selectedMealType] ?: Constants.DEFAULT_MEAL_TYPE
            val selectedMealTypeId = dataStorePreference[PreferenceKeys.selectedMealTypeId] ?: 0
            val selectedDietType = dataStorePreference[PreferenceKeys.selectedDietType] ?: Constants.DEFAULT_DIET_TYPE
            val selectedDietTypeId = dataStorePreference[PreferenceKeys.selectedDietTypeId] ?: 0

            // Return data of Map type
            MealAndDietType(
                selectedMealType,
                selectedMealTypeId,
                selectedDietType,
                selectedDietTypeId
            )
        }

    val readBackOnline: Flow<Boolean> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { dataStorePreference ->
            // Convert the Datastore Value to Map
            val backOnline = dataStorePreference[PreferenceKeys.backOnline] ?: false

            // Return data of Map type
            backOnline
        }
}