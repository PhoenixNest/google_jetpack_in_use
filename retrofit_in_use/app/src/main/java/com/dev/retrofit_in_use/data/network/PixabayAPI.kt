package com.dev.retrofit_in_use.data.network

import com.dev.retrofit_in_use.models.Pixabay
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface PixabayAPI {

    @GET("api/")
    suspend fun getPixabay(
        @QueryMap queries: Map<String, String>
    ): Response<Pixabay>

}