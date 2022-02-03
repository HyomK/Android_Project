package com.likefirst.btos.data.remote.posting.view

import com.likefirst.btos.data.remote.posting.response.Reply

interface ReplyView {
    fun onReplyLoading()
    fun onReplySuccess(replyList : ArrayList<Reply>)
    fun onReplyFailure(code : Int, message : String)
}