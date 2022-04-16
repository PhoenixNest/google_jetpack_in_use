package com.example.book_weather_report_example.logic.dao

import android.content.Context
import androidx.core.content.edit
import com.example.book_weather_report_example.WeatherApplication
import com.example.book_weather_report_example.logic.model.Place
import com.google.gson.Gson

object PlaceDao {
    fun savePlace(place: Place) {
        sharedPreferences().edit {
            putString("place", Gson().toJson(place))
        }
    }

    fun getSavePlace(): Place {
        val placeJson = sharedPreferences().getString("place", "")
        return Gson().fromJson(placeJson, Place::class.java)
    }

    fun isPlaceSaved() = sharedPreferences().contains("place")

    private fun sharedPreferences() =
        WeatherApplication.context.getSharedPreferences(
            "book_weather_report_example",
            Context.MODE_PRIVATE
        )
}