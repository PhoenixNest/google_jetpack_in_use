package com.dev.retrofit_in_use.data

import com.dev.retrofit_in_use.data.local.PixabayDao
import com.dev.retrofit_in_use.data.local.entities.PixabayEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDatasource @Inject constructor(
    private val pixabayDao: PixabayDao
) {

    fun readDatabase(): Flow<List<PixabayEntity>> {
        return pixabayDao.readDatabase()
    }

    suspend fun insertPixabay(pixabayEntity: PixabayEntity) {
        pixabayDao.insertData(pixabayEntity)
    }

}