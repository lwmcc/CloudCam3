package com.mccarty.cloudcam3.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "image")
class ImageEntity(
    //@ColumnInfo(name = "user_name")var userName: String?,
        userName: String?,
    @ColumnInfo(name = "file_name")var fileName: String?,
    @ColumnInfo(name = "local_file_path")var localFilePath: String?,
    @ColumnInfo(name = "file_extension")var fileExtension: String = "",
    @ColumnInfo(name = "latitude")var latitude: Long = 0L,
    @ColumnInfo(name = "longitude")var longitude: Long = 0L,
    @ColumnInfo(name = "time")var time: Long = 0L,
    @ColumnInfo(name = "private_image")var privateImage: Boolean = true,
    @PrimaryKey(autoGenerate = true) var uid: Int = 0): MediaEntity(userName) {



}