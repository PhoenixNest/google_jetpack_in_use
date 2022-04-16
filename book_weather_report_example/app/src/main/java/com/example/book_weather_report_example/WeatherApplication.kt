package com.example.book_weather_report_example

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

class WeatherApplication : Application() {
    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context

        const val TOKEN = "your api key"
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }
}