package com.example.book_weather_report_example.ui.weather

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.book_weather_report_example.logic.Repository
import com.example.book_weather_report_example.logic.model.Location

class WeatherViewModel : ViewModel() {
    private val locationLiveData = MutableLiveData<Location>()

    var locationLng = ""

    var locationLat = ""

    var placeName = ""

    val weatherLiveData = Transformations.switchMap(locationLiveData) { location ->
        Repository.refreshWeather(location.lng, location.lat)
    }

    fun refreshWeather(lng: String, lat: String) {
        locationLiveData.value = Location(lng, lat)
    }
}