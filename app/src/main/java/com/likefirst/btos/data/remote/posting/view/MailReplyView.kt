package com.likefirst.btos.data.remote.posting.view



import com.likefirst.btos.data.remote.posting.response.MailReplyDetailResponse

interface MailReplyView {
    fun onReplyLoading()
    fun onReplySuccess(replyList : ArrayList<MailReplyDetailResponse>)
    fun onReplyFailure(code : Int, message : String)
}