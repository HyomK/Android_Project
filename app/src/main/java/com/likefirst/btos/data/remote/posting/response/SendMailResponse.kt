package com.likefirst.btos.data.remote.posting.response

data class SendLetterRequest (  val userIdx : Int , val content:String?)

data class SendReplyRequest(
    val replierIdx : Int,
    val receiverIdx : Int,
    val firstHistoryType : String,
    val sendIdx : Int,
    val content: String
)

data class SendReplyResponse(
    val replyIdx : Int,
    val receiverIdx : Int,
    val senderNickName:String
)

