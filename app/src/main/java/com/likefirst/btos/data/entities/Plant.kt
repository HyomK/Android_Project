package com.likefirst.btos.data.entities
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize


@Parcelize
@Entity(tableName = "PlantTable")
data class Plant(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "plantIdx") var plantIdx: Int= 0,
    @ColumnInfo(name = "plantName") var plantName: String,
    @ColumnInfo(name = "plantImgUrl") var plantImgUrl: String,
    @ColumnInfo(name = "plantInfo") var plantInfo: String,
    @ColumnInfo(name = "plantPrice") var plantPrice: Int= 0,
    @ColumnInfo(name = "maxLevel") var maxLevel: Int= 0,
    @ColumnInfo(name = "currentLevel") var currentLevel: Int= 0,
    @ColumnInfo(name = "plantStatus") var plantStatus:String,
    @ColumnInfo(name = "isOwn") var isOwn:Boolean,

): Parcelable
