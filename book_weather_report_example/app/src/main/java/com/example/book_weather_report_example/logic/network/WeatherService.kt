package com.example.book_weather_report_example.logic.network

import com.example.book_weather_report_example.WeatherApplication
import com.example.book_weather_report_example.logic.model.DailyResponse
import com.example.book_weather_report_example.logic.model.RealtimeResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface WeatherService {
    // 于获取实时的天气信息
    @GET("v2.5/${WeatherApplication.TOKEN}/{lng},{lat}/realtime.json")
    fun getRealtimeWeather(
        @Path("lng") lng: String,
        @Path("lat") lat: String
    ): Call<RealtimeResponse>

    // 获取未来的天气信息
    @GET("v2.5/${WeatherApplication.TOKEN}/{lng},{lat}/daily.json")
    fun getDailyWeather(
        @Path("lng") lng: String,
        @Path("lat") lat: String
    ): Call<DailyResponse>

}