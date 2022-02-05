package com.likefirst.btos.data.remote.posting.view

import com.likefirst.btos.data.remote.posting.response.Diary


interface MailDiaryView {
    fun onDiaryLoading()
    fun onDiarySuccess(diaryList : Diary)
    fun onDiaryFailure(code : Int, message : String)
}