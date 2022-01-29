package com.likefirst.btos.data.entities

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "UserTable")
data class User (
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "email") var email: String= "",
    @ColumnInfo(name = "nickName") var nickName: String="",
    @ColumnInfo(name = "birth") var birth: Int = 0
): Parcelable