package com.dev.retrofit_in_use.module.interceptor

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class LogInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain.request()

        // Log.d("TAG", "intercept request url: ${request.url()}")

        println("intercept request url: ${request.url}")

        val response = chain.proceed(request)

        // Log.d("TAG", "intercept response body: ${response.body()}")

        println("intercept response body: ${response.body}")

        return response
    }
}