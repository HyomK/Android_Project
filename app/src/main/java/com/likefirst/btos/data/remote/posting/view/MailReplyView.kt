package com.likefirst.btos.data.remote.posting.view

import com.likefirst.btos.data.remote.posting.response.MailInfoResponse
import com.likefirst.btos.data.remote.posting.response.MailReplyResponse
import com.likefirst.btos.data.remote.posting.response.ReplyInfo

interface MailReplyView {
    fun onReplyLoading()
    fun onReplySuccess(reply : MailInfoResponse)
    fun onReplyFailure(code : Int, message : String)
}