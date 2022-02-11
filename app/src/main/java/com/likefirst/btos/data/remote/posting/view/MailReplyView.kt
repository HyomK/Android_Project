package com.likefirst.btos.data.remote.posting.view



import com.likefirst.btos.data.remote.posting.response.MailReplyDetailResponse
import com.likefirst.btos.data.remote.posting.response.ReplyInfo

interface MailReplyView {
    fun onReplyLoading()
    fun onReplySuccess(reply : ReplyInfo)
    fun onReplyFailure(code : Int, message : String)
}