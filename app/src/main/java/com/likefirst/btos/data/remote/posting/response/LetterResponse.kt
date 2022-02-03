package com.likefirst.btos.data.remote.posting.response

import com.google.gson.annotations.SerializedName

data class Letter(

    @SerializedName("letterIdx") val letterIdx : Int,
    @SerializedName("replierIdx") val replierIdx : Int,
    @SerializedName("receiverIdx") val receiverIdx : Int,
    @SerializedName("content") val content :String
)


data class LetterResponse(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code : Int,
    @SerializedName("message") val message : String,
    @SerializedName("result") val result : ResultLetter
)

data class  ResultLetter(
    @SerializedName("type") val type : String,
    @SerializedName("content") val content : Letter,
    @SerializedName("senderFontIdx") val senderFontIdx : Int
)