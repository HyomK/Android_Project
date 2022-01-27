package com.likefirst.btos.data.remote.response

import com.google.gson.annotations.SerializedName

data class Login(
    @SerializedName("userIdx") val userIdx : Int,
    @SerializedName("jwt") val jwt : String?
)

data class LoginResponse(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code : Int,
    @SerializedName("message") val message : String,
    @SerializedName("result") val result : Login
)