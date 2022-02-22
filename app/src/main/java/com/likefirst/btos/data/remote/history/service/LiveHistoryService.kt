package com.likefirst.btos.data.remote.history.service

import com.likefirst.btos.ApplicationClass
import com.likefirst.btos.data.entities.BasicHistory
import com.likefirst.btos.data.entities.Content
import com.likefirst.btos.data.entities.SenderList
import com.likefirst.btos.data.remote.history.response.HistoryBaseResponse
import com.likefirst.btos.data.remote.history.response.HistoryDetailResponse
import com.likefirst.btos.data.remote.history.response.HistorySenderDetailResponse
import com.likefirst.btos.data.remote.history.view.*
import com.likefirst.btos.ui.history.HistoryBasicRecyclerViewAdapter
import com.likefirst.btos.ui.history.HistoryDetailRecyclerViewAdapter
import com.likefirst.btos.utils.RetrofitInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LiveHistoryService {
    private lateinit var historySenderView : LiveHistorySenderView
    private lateinit var historyDLView: LiveHistoryDiaryandLetterView
    private lateinit var senderDetailView: LiveSenderDetailView
    private lateinit var historyDetailView: LiveHistoryDetailView

    private val HistoryService = ApplicationClass.retrofit.create(RetrofitInterface::class.java)

    fun setHistorySenderView(historySenderView : LiveHistorySenderView){
        this.historySenderView = historySenderView
    }

    fun sethistoryDiaryView(historyDLView : LiveHistoryDiaryandLetterView){
        this.historyDLView = historyDLView
    }

    fun setSenderDetailView(senderDetailView: LiveSenderDetailView){
        this.senderDetailView = senderDetailView
    }

    fun setHistoryDetailView(historyDetailView: LiveHistoryDetailView){
        this.historyDetailView = historyDetailView
    }

    fun sender(userIdx : Int, pageNum : Int, filtering : String, search :  String?){
        historySenderView.onHistorySenderLoading()

        HistoryService.historyListSender(userIdx, pageNum, filtering, search).enqueue(object :
            Callback<HistoryBaseResponse<BasicHistory<SenderList>>> {
            override fun onResponse(
                call: Call<HistoryBaseResponse<BasicHistory<SenderList>>>,
                response: Response<HistoryBaseResponse<BasicHistory<SenderList>>>,
            ) {
                val senderList: HistoryBaseResponse<BasicHistory<SenderList>> = response.body()!!

                when(senderList.code){
                    1000->historySenderView.onHistorySenderSuccess(senderList.result, senderList.pageInfo)
                    else->historySenderView.onHistorySenderFailure(senderList.code,senderList.message)
                }
            }
            override fun onFailure(
                call: Call<HistoryBaseResponse<BasicHistory<SenderList>>>,
                t: Throwable,
            ) {
                historySenderView.onHistorySenderFailure(400,"네트워크 오류가 발생했습니다.")
            }
        })

    }

    fun diaryletter(userIdx : Int, pageNum : Int, filtering : String, search :  String?){
        historyDLView.onHistoryDiaryLoading()

        HistoryService.historyListDiaryLetter(userIdx, pageNum, filtering, search).enqueue(object :
            Callback<HistoryBaseResponse<BasicHistory<Content>>> {
            override fun onResponse(
                call: Call<HistoryBaseResponse<BasicHistory<Content>>>,
                response: Response<HistoryBaseResponse<BasicHistory<Content>>>,
            ) {
                val DLlist: HistoryBaseResponse<BasicHistory<Content>> = response.body()!!

                when(DLlist.code){
                    1000->historyDLView.onHistoryDiarySuccess(DLlist.result, DLlist.pageInfo)
                    else->historyDLView.onHistoryDiaryFailure(DLlist.code,DLlist.message)
                }
            }
            override fun onFailure(
                call: Call<HistoryBaseResponse<BasicHistory<Content>>>,
                t: Throwable,
            ) {
                historyDLView.onHistoryDiaryFailure(400,"네트워크 오류가 발생했습니다.")
            }
        })
    }

    fun senderDetail(userIdx : Int, senderNickName : String, pageNum : Int, search : String?){
        senderDetailView.onSenderDetailLoading()

        HistoryService.historyListSenderDetail(userIdx, senderNickName, pageNum, search).enqueue(object :
            Callback<HistorySenderDetailResponse> {
            override fun onResponse(
                call: Call<HistorySenderDetailResponse>,
                response: Response<HistorySenderDetailResponse>,
            ) {
                val detailList: HistorySenderDetailResponse = response.body()!!

                when(detailList.code){
                    1000->senderDetailView.onSenderDetailSuccess(detailList.result, detailList.pageInfo)
                    else->senderDetailView.onSenderDetailFailure(detailList.code,detailList.message)
                }
            }
            override fun onFailure(
                call: Call<HistorySenderDetailResponse>,
                t: Throwable,
            ) {
                senderDetailView.onSenderDetailFailure(400,"네트워크 오류가 발생했습니다.")
            }
        })
    }

    fun historyDetail(userIdx : Int, type : String, typeIdx : Int){
        historyDetailView.onHistoryDetailLoading()

        HistoryService.historyDetailList(userIdx, type, typeIdx).enqueue(object:
            Callback<HistoryDetailResponse> {
            override fun onResponse(
                call: Call<HistoryDetailResponse>,
                response: Response<HistoryDetailResponse>,
            ) {
                val historyList: HistoryDetailResponse = response.body()!!

                when(historyList.code){
                    1000->historyDetailView.onHistoryDetailSuccess(historyList.result)
                    else->historyDetailView.onHistoryDetailFailure(historyList.code,historyList.message)
                }
            }

            override fun onFailure(call: Call<HistoryDetailResponse>, t: Throwable) {
                historyDetailView.onHistoryDetailFailure(400,"네트워크 오류가 발생했습니다.")

            }

        })
    }
}