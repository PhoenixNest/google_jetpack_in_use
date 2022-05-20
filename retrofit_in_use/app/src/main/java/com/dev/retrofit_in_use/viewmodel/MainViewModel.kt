package com.dev.retrofit_in_use.viewmodel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.*
import com.dev.retrofit_in_use.data.Repository
import com.dev.retrofit_in_use.data.local.entities.PixabayEntity
import com.dev.retrofit_in_use.models.Pixabay
import com.dev.retrofit_in_use.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    application: Application,
    private val repository: Repository
) : AndroidViewModel(application) {

    /* ======================== Local ROOM Database ======================== */

    // Cast the Flow-Type into LiveData-Type
    val pixabayData: LiveData<List<PixabayEntity>> = repository.local.readDatabase().asLiveData()

    // This scope will be canceled when ViewModel will be cleared
    private fun insertPixabay(pixabayEntity: PixabayEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.insertPixabay(pixabayEntity)
        }
    }

    private fun offlineCacheRecipes(foodRecipesData: Pixabay) {
        val pixabayEntity = PixabayEntity(foodRecipesData)
        insertPixabay(pixabayEntity)
    }

    /* ======================== Remote Server ======================== */

    var pixabayResponse: MutableLiveData<NetworkResult<Pixabay>> = MutableLiveData()

    fun getPixabayData(queries: Map<String, String>) {
        // This scope will be canceled when ViewModel will be cleared
        viewModelScope.launch {
            getPixabaySafeCall(queries)
        }
    }

    private suspend fun getPixabaySafeCall(queries: Map<String, String>) {
        pixabayResponse.value = NetworkResult.Loading()
        if (hasInternetConnection()) {
            try {
                val response = repository.remote.getPixabay(queries)
                pixabayResponse.value = handlePixabayResponse(response)

                // Save newest Data into Database
                val foodRecipesData = pixabayResponse.value!!.data
                if (foodRecipesData != null) {
                    offlineCacheRecipes(foodRecipesData)
                }

            } catch (exception: Exception) {
                pixabayResponse.value = NetworkResult.Error(message = "Image not found.")
            }
        } else {
            pixabayResponse.value = NetworkResult.Error(message = "No Internet Connection...")
        }
    }

    private fun handlePixabayResponse(response: Response<Pixabay>): NetworkResult<Pixabay> {
        when {
            /* ======================== Failure ======================== */
            // Request Timeout
            response.message().toString().contains("timeout") -> {
                return NetworkResult.Error(message = "Timeout")
            }

            // 402 -> Request reached limited
            response.code() == 402 -> {
                return NetworkResult.Error(message = "API Key Limited.")
            }

            // Response Body is Null or Empty
            response.body()!!.hits.isNullOrEmpty() -> {
                return NetworkResult.Error(message = "Image not found")
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