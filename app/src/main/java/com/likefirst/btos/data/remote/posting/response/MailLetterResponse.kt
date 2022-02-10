package com.likefirst.btos.data.remote.posting.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MailLetterDetailResponse(
    @SerializedName("letterIdx") val letterIdx : Int,
    @SerializedName("replierIdx") val replierIdx : Int,
    @SerializedName("receiverIdx") val receiverIdx : Int,
    @SerializedName("content") val content :String
):Parcelable


data class MailLetterResponse(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code : Int,
    @SerializedName("message") val message : String,
    @SerializedName("result") val result : LetterInfo
)

@Parcelize
data class LetterInfo(
    @SerializedName("type") val type : String,
    @SerializedName("content") val content : MailLetterDetailResponse,
    @SerializedName("senderNickName") val senderNickname :String,
    @SerializedName("senderFontIdx") val senderFontIdx : Int
):Parcelable