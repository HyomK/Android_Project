package com.likefirst.btos.data.remote.posting.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


@Parcelize
data class ReplyInfo(
    @SerializedName("replyIdx") val replyIdx : Int,
    @SerializedName("content") val content : String,

):Parcelable

@Parcelize
data class MailReplyResponse(
    @SerializedName("mail") val mail :ReplyInfo,
    @SerializedName("senderNickName") val senderNickName : String,
    @SerializedName("senderFontIdx") val senderFontIdx : Int,
):Parcelable

