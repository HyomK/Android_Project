package com.likefirst.btos.data.remote.posting.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


data class MailReplyResponse(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code : Int,
    @SerializedName("message") val message : String,
    @SerializedName("result") val result : ReplyInfo
)

@Parcelize
data class ReplyInfo(
    @SerializedName("type") val type : String,
    @SerializedName("content") val content : MailReplyDetailResponse,
    @SerializedName("senderNickName") val senderNickName : String,
    @SerializedName("senderFontIdx") val senderFontIdx : Int,
):Parcelable

@Parcelize
data class MailReplyDetailResponse(
    @SerializedName("replyIdx") val replyIdx : Int,
    @SerializedName("content") val content :String
):Parcelable

