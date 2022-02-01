package com.likefirst.btos.data.entities
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize



@Parcelize
@Entity(tableName = "PlantTable")
data class Plant(
    @PrimaryKey(autoGenerate = false)

    @ColumnInfo(name = "plantIdx")
    @SerializedName("plantIdx") val plantIdx: Int,

    @ColumnInfo(name = "plantName")
    @SerializedName( "plantName") var plantName: String,

    @ColumnInfo(name = "plantImgUrl")
    @SerializedName( "plantImgUrl") var plantImgUrl: String?="",

    @ColumnInfo(name = "plantInfo")
    @SerializedName( "plantInfo") var plantInfo: String,

    @ColumnInfo(name = "plantPrice")
    @SerializedName( "plantPrice") var plantPrice: Int,

    @ColumnInfo(name = "maxLevel")
    @SerializedName("maxLevel") var maxLevel: Int,

    @ColumnInfo(name = "currentLevel")
    @SerializedName( "currentLevel") var currentLevel: Int= 0,

    @ColumnInfo(name = "plantStatus")
    @SerializedName( "plantStatus") var plantStatus:String,

    @ColumnInfo(name = "isOwn")
    @SerializedName( "isOwn") var isOwn:Boolean,
) : Parcelable
