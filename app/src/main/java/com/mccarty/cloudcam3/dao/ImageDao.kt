package com.mccarty.cloudcam3.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.mccarty.cloudcam3.db.ImageEntity
import com.mccarty.cloudcam3.db.MediaEntity

@Dao
interface ImageDao {
    @Query("SELECT * FROM image")
    fun getAll(): MutableList<ImageEntity>

    @Insert
    fun insertAll(vararg images: ImageEntity)

    @Insert
    fun insertImageEntity(image: ImageEntity)
}