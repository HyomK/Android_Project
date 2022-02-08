package com.likefirst.btos.data.entities.firebase


import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize


@Parcelize
@Entity(tableName = "FCMTable")
data class UserDTO(
    @PrimaryKey(autoGenerate = false)

    @ColumnInfo(name = "email")
    var email : String="",

    @ColumnInfo(name = "fcmToken")
    var fcmToken : String = ""

) : Parcelable