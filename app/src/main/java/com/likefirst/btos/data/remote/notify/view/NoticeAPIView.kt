package com.likefirst.btos.data.remote.notify.view

import com.likefirst.btos.data.remote.notify.response.NoticeDetailResponse
import com.likefirst.btos.ui.view.main.CustomDialogFragment

interface NoticeAPIView {
    fun onNoticeAPIError(Dialog : CustomDialogFragment)
    fun onNoticeAPISuccess(noticeList : ArrayList<NoticeDetailResponse>)
    fun onNoticeAPIFailure(code : Int, message : String)
}

interface NotificationListener{
    fun onPushNow()
}

interface SystemPushAlarmView {
    fun onSystemPushAlarmLoading()
    fun onSystemPushAlarmSuccess()
    fun onSystemPushAlarmFailure(code : Int)
}