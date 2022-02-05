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
    @ColumnInfo(name = "userIdx") var userIdx: Int? = 1,
    @ColumnInfo(name = "email") var email: String="",
    @ColumnInfo(name = "nickName") var nickName: String="",
    @ColumnInfo(name = "birth") var birth: Int = 0,
    @ColumnInfo(name = "selectedPlantIdx") var selectedPlantIdx: Int? = 1,
    var isSad: Boolean? = false,
    @ColumnInfo(name = "isPremium") var premium: String? = "free",
    @ColumnInfo(name = "recOthers") var recOthers: Boolean? = true,
    @ColumnInfo(name = "recSimilarAge") var recSimilarAge: Boolean? = true,
    @ColumnInfo(name = "fontIdx") var fontIdx: Int? = 1,
    @ColumnInfo(name = "pushAlarm") var pushAlarm: Boolean? = true
): Parcelable

data class UserSign(
    @ColumnInfo(name = "email") var email: String="",
    @ColumnInfo(name = "nickName") var nickName: String="",
    @ColumnInfo(name = "birth") var birth: Int = 0,
)

data class UserIsSad(
    @ColumnInfo(name = "isSad") var isSad: Boolean?
)

data class UserName(
    @ColumnInfo(name = "nickName") var nickName: String
)

data class UserBirth(
    @ColumnInfo(name = "birth") var birth: Int
)