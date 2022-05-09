package com.dev.online_food_recipes_example.module_dependency_injection

import com.dev.online_food_recipes_example.data.network.FoodRecipesApi
import com.dev.online_food_recipes_example.utils.Constants.Companion.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

// All those are binding inside the NetworkModule will be available in an application component
@Module
// After Dagger-Hilt v2.31, ApplicationComponent::class has been removed by Google, use SingletonComponent::class instead
@InstallIn(/*ApplicationComponent::class*/ SingletonComponent::class)
object NetworkModule {

    // Use Singleton annotation to tell Hilt that we just need only one INSTANCE
    @Singleton
    // Because of we are use Okhttp/Retrofit library,
    // which is an external library which not create by us
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .readTimeout(15, TimeUnit.SECONDS)
            .connectTimeout(15, TimeUnit.SECONDS)
            .build()
    }

    @Singleton
    @Provides
    fun provideConverterFactory(): GsonConverterFactory {
        return GsonConverterFactory.create()
    }

    @Singleton
    @Provides
    fun provideRetrofitInstance(
        okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(gsonConverterFactory)
            .build()
    }

    @Singleton
    @Provides
    fun provideApiService(retrofit: Retrofit): FoodRecipesApi {
        return retrofit.create(FoodRecipesApi::class.java)
    }

}