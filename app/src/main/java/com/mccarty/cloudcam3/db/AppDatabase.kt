package com.mccarty.cloudcam3.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mccarty.cloudcam3.dao.ImageDao
import com.mccarty.cloudcam3.dao.VideoDao

@Database(entities = arrayOf(ImageEntity::class, VideoEntity::class), version = 1, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {
    abstract fun imageDao(): ImageDao
    abstract fun videoDao(): VideoDao
}