package com.likefirst.btos.data.remote.posting.view
import com.likefirst.btos.data.remote.posting.response.MailDiaryResponse
import com.likefirst.btos.data.remote.posting.response.MailInfoResponse
import com.likefirst.btos.data.remote.posting.response.PostDiaryResponse


interface MailDiaryView {
    fun onDiaryLoading()
    fun onDiarySuccess(diaryList : MailInfoResponse)
    fun onDiaryFailure(code : Int, message : String)
}

interface PostDiaryView{
    fun onDiaryPostLoading()
    fun onDiaryPostSuccess(result: PostDiaryResponse)
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