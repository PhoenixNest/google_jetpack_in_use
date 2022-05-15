package com.dev.retrofit_in_use.data.local.entities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.dev.retrofit_in_use.models.Pixabay
import com.dev.retrofit_in_use.utils.Constants
import kotlinx.parcelize.Parcelize

@Entity(tableName = Constants.PIXABAY_TABLE)
@Parcelize
class PixabayEntity(
    var pixabay: Pixabay
) : Parcelable {
    @PrimaryKey(autoGenerate = false)
    var id: Int = 0
}