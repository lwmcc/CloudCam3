package com.mccarty.cloudcam3.db

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity
open class MediaEntity(@ColumnInfo(name = "user_name")var userName: String?) {
    constructor(mediaEntity: MediaEntity): this(mediaEntity.userName)
}