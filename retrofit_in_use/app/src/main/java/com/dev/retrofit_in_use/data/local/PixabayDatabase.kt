package com.dev.retrofit_in_use.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.dev.retrofit_in_use.data.local.entities.PixabayEntity

@Database(
    entities = [PixabayEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(PixabayTypeConverter::class)
abstract class PixabayDatabase : RoomDatabase() {
    abstract fun pixabayDao(): PixabayDao
}