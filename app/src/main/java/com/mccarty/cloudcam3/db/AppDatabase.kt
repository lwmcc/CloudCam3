package com.mccarty.cloudcam3.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.mccarty.cloudcam3.dao.ImageDao
import com.mccarty.cloudcam3.dao.VideoDao

@Database(entities = [ImageEntity::class, VideoEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {
    abstract fun imageDao(): ImageDao
    abstract fun videoDao(): VideoDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context:Context): AppDatabase{
            if (INSTANCE == null){
                INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "CloudCam3db")
                        .build()
            }
            return INSTANCE as AppDatabase
        }
    }
}