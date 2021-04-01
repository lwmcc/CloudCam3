package com.mccarty.cloudcam3.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.mccarty.cloudcam3.db.ImageEntity
import com.mccarty.cloudcam3.db.MediaEntity

@Dao
interface ImageDao {
    @Query("SELECT * FROM image")
    suspend fun getAll(): Array<ImageEntity>

    @Insert
    fun insertAll(vararg images: ImageEntity)

    @Insert
    fun insertImageEntity(image: ImageEntity)

    @Delete
    suspend fun deleteImage(image: ImageEntity)
}