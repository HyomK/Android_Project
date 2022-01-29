package com.likefirst.btos.data.remote.response

import com.google.gson.annotations.SerializedName

data class Mailbox(
    @SerializedName("type") val type: String,
    @SerializedName("idx") val idx : Int,
    @SerializedName("senderNickName") val senderNickName : String,
    @SerializedName("sendAt") val sendAt : String,
    @SerializedName("hasSealing") val hasSealing : Boolean
)




data class MailboxResponse(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code : Int,
    @SerializedName("message") val message : String,
    @SerializedName("result") val result : ArrayList<Mailbox>
)