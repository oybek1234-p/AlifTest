package com.uz.aliftest.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class Post(
    @PrimaryKey
    var id: String = "",
    @ColumnInfo(name = "userId")
    var userId: String = "",
    @ColumnInfo(name = "title")
    var title: String = "",
    @ColumnInfo(name = "body")
    var body: String = ""
)