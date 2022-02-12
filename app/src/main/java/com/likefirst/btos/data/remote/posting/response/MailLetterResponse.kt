package com.likefirst.btos.data.remote.posting.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LetterInfo(
    @SerializedName("letterIdx") val letterIdx : Int,
    @SerializedName("content") val content :String
):Parcelable



@Parcelize
data class MailLetterResponse(
    @SerializedName("mail") val mail : LetterInfo,
    @SerializedName("senderNickName") val senderNickname :String,
    @SerializedName("senderFontIdx") val senderFontIdx : Int
):Parcelable