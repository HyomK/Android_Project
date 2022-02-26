package com.likefirst.btos.data.remote.notify.response

import com.google.gson.annotations.SerializedName

data class Alarm(
    @SerializedName("alarmIdx") val alarmIdx:Int,
    @SerializedName("content") val content: String,
    @SerializedName("createdAt")val createdAt:String

)

data class AlarmInfo(
    @SerializedName("userIdx") val userIdx:Int,
    @SerializedName("alarmType") val alarmType: String,
    @SerializedName("reqParamIdx")val reqParamIdx:Int

)