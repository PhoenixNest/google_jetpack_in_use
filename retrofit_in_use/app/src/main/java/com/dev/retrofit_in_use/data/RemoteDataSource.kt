package com.dev.retrofit_in_use.data

import com.dev.retrofit_in_use.data.network.PixabayAPI
import com.dev.retrofit_in_use.models.Pixabay
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val pixabayAPI: PixabayAPI
) {

    suspend fun getPixabay(queries: Map<String, String>): Response<Pixabay> {
        return pixabayAPI.getPixabay(queries)
    }
}