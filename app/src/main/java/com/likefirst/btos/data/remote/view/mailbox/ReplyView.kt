package com.likefirst.btos.data.remote.view.mailbox

import com.likefirst.btos.data.remote.response.Mailbox
import com.likefirst.btos.data.remote.response.Reply

interface ReplyView {
    fun onReplyLoading()
    fun onReplySuccess(replyList : ArrayList<Reply>)
    fun onReplyFailure(code : Int, message : String)
}