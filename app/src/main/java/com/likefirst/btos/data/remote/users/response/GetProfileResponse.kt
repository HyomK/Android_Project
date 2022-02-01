package com.likefirst.btos.data.remote.users.response

import com.google.gson.annotations.SerializedName
import com.likefirst.btos.data.entities.User

data class GetProfileResponse(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code : Int,
    @SerializedName("message") val message : String,
    @SerializedName("result") val result : User
)