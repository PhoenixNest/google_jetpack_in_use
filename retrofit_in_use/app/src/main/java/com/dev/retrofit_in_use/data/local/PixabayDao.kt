package com.dev.retrofit_in_use.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dev.retrofit_in_use.data.local.entities.PixabayEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PixabayDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertData(pixabayEntity: PixabayEntity)

    @Query("select * from pixabay_table order by id asc")
    fun readDatabase(): Flow<List<PixabayEntity>>

}