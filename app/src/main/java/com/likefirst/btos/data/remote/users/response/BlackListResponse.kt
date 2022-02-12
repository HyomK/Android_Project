package com.likefirst.btos.data.remote.users.response

import com.google.gson.annotations.SerializedName

data class BlackList(
    @SerializedName("blockIdx") val blockIdx : Int,
    @SerializedName("blockedUserIdx") val blockedUserIdx : Int
)

data class BlockUser(
    @SerializedName("blockedNickname")val userName:String,
    @SerializedName("blockIdx") val blockIdx: Int,
    @SerializedName("blockedUserIdx") val blockedUserIdx : Int,

)