package com.likefirst.btos.data.entities

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey

class User (
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "email") var email: String= "",
    @ColumnInfo(name = "nickName") var nickName: String="",
    @ColumnInfo(name = "birth") var birth: Int = 0
)