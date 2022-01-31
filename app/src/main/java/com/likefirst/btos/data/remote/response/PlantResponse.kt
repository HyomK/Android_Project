package com.likefirst.btos.data.remote.response

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.likefirst.btos.data.entities.Plant

data class PlantRequest(
    @SerializedName("userIdx") val userIdx : Int,
    @SerializedName("plantIdx") val plantIdx : Int
)

data class Plant(
    @SerializedName("plantIdx") val plantIdx: Int,
    @SerializedName( "plantName") var plantName: String,
    @SerializedName( "plantImgUrl") var plantImgUrl: String,
    @SerializedName( "plantInfo") var plantInfo: String,
    @SerializedName( "plantPrice") var plantPrice: Int,
    @SerializedName("maxLevel") var maxLevel: Int,
    @SerializedName( "currentLevel") var currentLevel: Int= 0,
    @SerializedName( "plantStatus") var plantStatus:String,
    @SerializedName( "isOwn") var isOwn:Boolean,
)


data class PlantResponse(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code : Int,
    @SerializedName("message") val message : String,
    @SerializedName("result") val result : ArrayList<Plant>
)