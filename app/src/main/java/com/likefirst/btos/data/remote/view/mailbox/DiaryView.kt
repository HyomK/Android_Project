package com.likefirst.btos.data.remote.view.mailbox

import com.likefirst.btos.data.remote.response.Diary


interface DiaryView {
    fun onDiaryLoading()
    fun onDiarySuccess(diaryList : Diary)
    fun onDiaryFailure(code : Int, message : String)
}