package com.dev.retrofit_in_use.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.dev.retrofit_in_use.utils.Constants

class PixabayViewModel(application: Application) : AndroidViewModel(application) {
    fun setUpQueries(): Map<String, String> {
        val queries: HashMap<String, String> = HashMap()

        queries[Constants.QUERY_API_KEY] = Constants.API_KEY
        queries[Constants.QUERY_PARAMETER] = "hummingbird"
        queries[Constants.QUERY_IMAGE_TYPE] = "photo"
        queries[Constants.QUERY_ORIENTATION] = "all"
        queries[Constants.QUERY_EDITORS_CHOICE] = "true"

        return queries
    }

}