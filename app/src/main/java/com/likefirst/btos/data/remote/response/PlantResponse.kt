package com.likefirst.btos.data.remote.response

import com.google.gson.annotations.SerializedName
import com.likefirst.btos.data.entities.Plant

data class PlantSelect(
    @SerializedName("userIdx") val userIdx : Int,
    @SerializedName("futurePlant") val futurePlant : Int
)

data class PlanId(
    @SerializedName("userIdx") val userIdx : Int,
    @SerializedName("plantIdx") val plantIdx : Int
)

data class PlantResponse(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code : Int,
    @SerializedName("message") val message : String,
    @SerializedName("result") val result : List<Plant>
)