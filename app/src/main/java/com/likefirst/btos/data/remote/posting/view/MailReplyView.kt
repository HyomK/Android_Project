package com.likefirst.btos.data.remote.posting.view

import com.likefirst.btos.data.remote.posting.response.MailReplyResponse
import com.likefirst.btos.data.remote.posting.response.ReplyInfo

interface MailReplyView {
    fun onReplyLoading()
    fun onReplySuccess(reply : MailReplyResponse)
    fun onReplyFailure(code : Int, message : String)
}