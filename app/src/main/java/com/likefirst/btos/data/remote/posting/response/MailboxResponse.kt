package com.likefirst.btos.data.remote.posting.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class Mailbox(
    @SerializedName("type") val type: String,
    @SerializedName("idx") val idx : Int,
    @SerializedName("senderNickName") val senderNickName : String,
    @SerializedName("sendAt") val sendAt : String,
    @SerializedName("hasSealing") val hasSealing : Boolean,
)


@Parcelize
data class MailInfoResponse(
    @SerializedName("firstHistoryType") val firstHistoryType :String,
    @SerializedName("type") val type: String,
    @SerializedName("typeIdx") val typeIdx : Int,
    @SerializedName("content") val content : String?,
    @SerializedName("emotionIdx") val emotionIdx : Int,
    @SerializedName("doneList") val doneList :ArrayList<String>?,
    @SerializedName("sendAt") val sendAt : String,
    @SerializedName("senderIdx") val senderIdx : Int,
    @SerializedName("senderNickName") val senderNickName :String,
    @SerializedName("senderActive") val senderActive :Boolean,
    @SerializedName("senderFontIdx") val senderFontIdx :Int
):Parcelable


data class MailboxResponse(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code : Int,
    @SerializedName("message") val message : String,
    @SerializedName("result") val result : ArrayList<Mailbox>
)