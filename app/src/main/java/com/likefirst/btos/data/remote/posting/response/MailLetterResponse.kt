package com.likefirst.btos.data.remote.posting.response

import com.google.gson.annotations.SerializedName

data class MailLetterDetailResponse(

    @SerializedName("letterIdx") val letterIdx : Int,
    @SerializedName("replierIdx") val replierIdx : Int,
    @SerializedName("receiverIdx") val receiverIdx : Int,
    @SerializedName("content") val content :String
)


data class MailLetterResponse(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code : Int,
    @SerializedName("message") val message : String,
    @SerializedName("result") val result : ResultLetter
)

data class  ResultLetter(
    @SerializedName("type") val type : String,
    @SerializedName("content") val content : MailLetterDetailResponse,
    @SerializedName("senderFontIdx") val senderFontIdx : Int
)