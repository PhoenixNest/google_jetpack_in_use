package com.dev.retrofit_in_use.data.local

import androidx.room.TypeConverter
import com.dev.retrofit_in_use.models.Pixabay
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class PixabayTypeConverter {

    private val gson = Gson()

    @TypeConverter
    fun pixabayToJsonString(pixabay: Pixabay): String {
        return gson.toJson(pixabay)
    }

    @TypeConverter
    fun jsonStringToPixabay(json: String): Pixabay {
        val type = object : TypeToken<Pixabay>() {}.type

        return gson.fromJson(json, type)
    }
}