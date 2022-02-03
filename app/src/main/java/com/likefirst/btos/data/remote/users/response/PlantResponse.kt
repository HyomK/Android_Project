package com.likefirst.btos.data.remote.users.response

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.likefirst.btos.data.entities.Plant


class PlantRequest(val userId : Int, val plantId:Int){
    @SerializedName("userIdx") val userIdx : Int = userId
    @SerializedName("plantIdx") val plantIdx :Int =plantId
}



data class PlantResponse(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code : Int,
    @SerializedName("message") val message : String,
    @SerializedName("result") val result : ArrayList<Plant>?
)