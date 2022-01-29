package com.likefirst.btos.data.remote.response

import com.google.gson.annotations.SerializedName

data class Reply(
    @SerializedName("replyIdx") val replyIdx : Int,
    @SerializedName("replierIdx") val replierIdx : Int,
    @SerializedName("receiverIdx") val receiverIdx : Int,
    @SerializedName("content") val content :String
)


data class ReplyResponse(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code : Int,
    @SerializedName("message") val message : String,
    @SerializedName("result") val result : ResultReply
)

data class ResultReply(
    @SerializedName("type") val type : String,
    @SerializedName("content") val content : ArrayList<Reply>,
    @SerializedName("senderFontIdx") val senderFontIdx : Int,
)