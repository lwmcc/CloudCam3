package com.mccarty.cloudcam3.repository

import com.mccarty.cloudcam3.db.AppDatabase
import com.mccarty.cloudcam3.db.ImageEntity
import javax.inject.Inject

class Repository @Inject constructor(private val appDatabase: AppDatabase) {
    suspend fun getAllImages(): Array<ImageEntity> {
        return appDatabase.imageDao().getAll()
    }

    suspend fun deleteImage(entity: ImageEntity) {
        appDatabase.imageDao().deleteImage(entity)
    }

    suspend fun insertEntity(entity: ImageEntity) {
        appDatabase.imageDao().insertImageEntity(entity)
    }
}