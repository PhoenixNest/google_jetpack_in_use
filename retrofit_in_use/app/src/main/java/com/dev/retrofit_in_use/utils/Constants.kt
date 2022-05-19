package com.dev.retrofit_in_use.utils

class Constants {
    companion object {
        const val BASE_URL = "https://pixabay.com/"
        const val API_KEY = "15455499-22cfacf3fc2820d332ff649aa"

        const val QUERY_API_KEY = "key"
        const val QUERY_PARAMETER = "q"
        const val QUERY_IMAGE_TYPE = "image_type"
        const val QUERY_ORIENTATION = "orientation"
        const val QUERY_EDITORS_CHOICE = "editors_choice"

        const val DATABASE_NAME = "pixabay_database"
        const val PIXABAY_TABLE = "pixabay_table"

        const val INTENT_ACTION_SELECT_PHOTO = 100
        const val INTENT_ACTION_TAKE_PHOTO = 200
    }
}