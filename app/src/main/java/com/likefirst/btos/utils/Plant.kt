package com.likefirst.btos.utils
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize


@Parcelize
@Entity(tableName = "PlantTable")
data class Plant(
    @PrimaryKey(autoGenerate = false) var plantIdx: Int= 0, // album의 pk는 임의로 지정해주기 위해 autogenerate 안씁니다.
    var plantName: String?="",
    var plantImgUrl: String? = "",
    var plantPrice: Int= 0,
    var maxLevel: Int= 0,
    var currentLevel: Int= 0,
    var userStatus:String? = ""

): Parcelable
