package com.likefirst.btos.data.remote.notify.view

import com.likefirst.btos.data.remote.notify.response.NoticeAPIResponse
import com.likefirst.btos.data.remote.notify.response.NoticeDetailResponse
import com.likefirst.btos.data.remote.posting.response.Mailbox
import com.likefirst.btos.ui.main.CustomDialogFragment

interface NoticeAPIView {
    fun onNoticeAPIError(Dialog : CustomDialogFragment)
    fun onNoticeAPISuccess(noticeList : ArrayList<NoticeDetailResponse>)
    fun onNoticeAPIFailure(code : Int, message : String)
}