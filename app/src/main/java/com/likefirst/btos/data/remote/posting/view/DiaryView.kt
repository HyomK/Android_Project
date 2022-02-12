package com.likefirst.btos.data.remote.posting.view

import com.likefirst.btos.data.remote.posting.response.Diary


interface DiaryView {
    fun onDiaryLoading()
    fun onDiarySuccess(diaryList : Diary)
    fun onDiaryFailure(code : Int, message : String)
}

interface PostDiaryView{
    fun onDiaryPostLoading()
    fun onDiaryPostSuccess()
    fun onDiaryPostFailure(code : Int)
}

interface UpdateDiaryView{
    fun onArchiveUpdateLoading()
    fun onArchiveUpdateSuccess()
    fun onArchiveUpdateFailure(code : Int)
}