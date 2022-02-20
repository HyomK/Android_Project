package com.likefirst.btos.data.remote.posting.view
import com.likefirst.btos.data.remote.posting.response.MailDiaryResponse


interface MailDiaryView {
    fun onDiaryLoading()
    fun onDiarySuccess(diaryList : MailDiaryResponse)
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

interface DeleteDiaryView{
    fun onDiaryDeleteLoading()
    fun onDiaryDeleteSuccess()
    fun onDiaryDeleteFailure(code : Int)
}