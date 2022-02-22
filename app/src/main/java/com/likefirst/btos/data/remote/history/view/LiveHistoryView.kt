package com.likefirst.btos.data.remote.history.view

import com.likefirst.btos.data.entities.*
import com.likefirst.btos.ui.history.HistoryBasicRecyclerViewAdapter
import com.likefirst.btos.ui.history.HistoryDetailRecyclerViewAdapter

interface LiveHistorySenderView {
    fun onHistorySenderLoading()
    fun onHistorySenderSuccess(response : BasicHistory<SenderList>, pageInfo: PageInfo)
    fun onHistorySenderFailure(code : Int, message : String)
}

interface LiveHistoryDiaryandLetterView {
    fun onHistoryDiaryLoading()
    fun onHistoryDiarySuccess(response : BasicHistory<Content>, pageInfo : PageInfo)
    fun onHistoryDiaryFailure(code : Int, message : String)
}

interface LiveSenderDetailView {
    fun onSenderDetailLoading()
    fun onSenderDetailSuccess(response : ArrayList<Content>, pageInfo : PageInfo)
    fun onSenderDetailFailure(code : Int, message : String)
}

interface LiveHistoryDetailView{
    fun onHistoryDetailLoading()
    fun onHistoryDetailSuccess(response: ArrayList<HistoryList>)
    fun onHistoryDetailFailure(code: Int, message: String)
}