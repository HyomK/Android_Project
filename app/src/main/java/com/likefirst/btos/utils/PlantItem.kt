package com.likefirst.btos.utils

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "PlantItemTable")
data class  PlantItem (
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "plantIdx")
    var plantIdx: Int= 0, // album의 pk는 임의로 지정해주기 위해 autogenerate 안씁니다.
    @ColumnInfo(name = "plantName") var plantName: String?="",
    @ColumnInfo(name = "plantImgUrl") var plantImgUrl: String? = "",
    @ColumnInfo(name = "plantPrice") var plantPrice: Int= 0,
    @ColumnInfo(name = "maxLevel") var maxLevel: Int= 0,
    @ColumnInfo(name = "Status") var Status:String? = ""

): Parcelable