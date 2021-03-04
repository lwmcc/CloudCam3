package com.mccarty.cloudcam3.db

import androidx.room.Database
import androidx.room.DatabaseConfiguration
import androidx.room.InvalidationTracker
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteOpenHelper
import com.mccarty.cloudcam3.dao.MediaDao

@Database(entities = arrayOf(MediaDao::class), version = 1)
abstract class AppDataBase: RoomDatabase() {
    abstract fun mediaDao(): MediaDao
    override fun createOpenHelper(config: DatabaseConfiguration?): SupportSQLiteOpenHelper {
        TODO("Not yet implemented")
    }

    override fun createInvalidationTracker(): InvalidationTracker {
        TODO("Not yet implemented")
    }

    override fun clearAllTables() {
        TODO("Not yet implemented")
    }
}