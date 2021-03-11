package com.mccarty.cloudcam3.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "image")
data class ImageEntity (
    @ColumnInfo(name = "user_name")val userName: String?,
    @ColumnInfo(name = "file_name")val fileName: String?,
    @ColumnInfo(name = "local_file_path")val localFilePath: String?,
    @ColumnInfo(name = "file_extension")val fileExtension: String = "",
    @ColumnInfo(name = "latitude")val latitude: Long = 0L,
    @ColumnInfo(name = "longitude")val longitude: Long = 0L,
    @ColumnInfo(name = "time")val time: Long = 0L,
    @ColumnInfo(name = "private_image")val privateImage: Boolean = true,
    @PrimaryKey val uid: Int)