package com.likefirst.btos.data.remote.history.view

import com.likefirst.btos.data.entities.*
import com.likefirst.btos.ui.view.history.HistoryBasicRecyclerViewAdapter
import com.likefirst.btos.ui.view.history.HistoryDetailRecyclerViewAdapter

interface HistorySenderView {
    fun onHistorySenderLoading()
    fun onHistorySenderSuccess(response : BasicHistory<SenderList>, pageInfo: PageInfo, recyclerViewAdapter : HistoryBasicRecyclerViewAdapter)
    fun onHistorySenderFailure(code : Int, message : String, recyclerViewAdapter : HistoryBasicRecyclerViewAdapter)
}

interface HistoryDiaryandLetterView {
    fun onHistoryDiaryLoading()
    fun onHistoryDiarySuccess(response : BasicHistory<Content>, pageInfo : PageInfo, recyclerViewAdapter: HistoryBasicRecyclerViewAdapter)
    fun onHistoryDiaryFailure(code : Int, message : String, recyclerViewAdapter : HistoryBasicRecyclerViewAdapter)
}

interface SenderDetailView {
    fun onSenderDetailLoading()
    fun onSenderDetailSuccess(response : ArrayList<Content>, pageInfo : PageInfo, recyclerViewAdapter: HistoryBasicRecyclerViewAdapter)
    fun onSenderDetailFailure(code : Int, message : String)
}

interface HistoryDetailView{
    fun onHistoryDetailLoading()
    fun onHistoryDetailSuccess(response: ArrayList<HistoryList>, recyclerViewAdapter: HistoryDetailRecyclerViewAdapter)
    fun onHistoryDetailFailure(code: Int, message: String)
}