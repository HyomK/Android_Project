package com.likefirst.btos.data.remote.view

import com.likefirst.btos.data.remote.response.Diary


interface DiaryView {
    fun onDiaryLoading()
    fun onDiarySuccess(diaryList : Diary)
    fun onDiaryFailure(code : Int, message : String)
}