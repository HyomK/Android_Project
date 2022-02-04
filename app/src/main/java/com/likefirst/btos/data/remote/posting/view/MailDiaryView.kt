package com.likefirst.btos.data.remote.posting.view

import com.likefirst.btos.data.remote.response.MailDiaryDetailResponse


interface MailDiaryView {
    fun onDiaryLoading()
    fun onDiarySuccess(diaryList : MailDiaryDetailResponse)
    fun onDiaryFailure(code : Int, message : String)
}