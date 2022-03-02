package com.likefirst.btos.data.remote.posting.view

import com.likefirst.btos.data.remote.posting.response.SendReplyResponse

interface SendReplyView {
    fun onSendReplyLoading()
    fun onSendReplySuccess(result : SendReplyResponse)
    fun onSendReplyFailure(code : Int, message : String)
}