package com.likefirst.btos.data.remote.response

import com.google.gson.annotations.SerializedName
import com.likefirst.btos.data.remote.posting.response.MailReplyDetailResponse
import com.likefirst.btos.data.remote.posting.response.MailResultReply

data class MailReplyDetailResponse(
    @SerializedName("replyIdx") val replyIdx : Int,
    @SerializedName("replierIdx") val replierIdx : Int,
    @SerializedName("receiverIdx") val receiverIdx : Int,
    @SerializedName("content") val content :String
)


data class MailReplyResponse(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code : Int,
    @SerializedName("message") val message : String,
    @SerializedName("result") val result : MailResultReply
)

data class MailResultReply(
    @SerializedName("type") val type : String,
    @SerializedName("content") val content : ArrayList<MailReplyDetailResponse>,
    @SerializedName("senderFontIdx") val senderFontIdx : Int,
)