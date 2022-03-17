package com.likefirst.btos.data.remote.history.service

import com.likefirst.btos.ApplicationClass.Companion.retrofit
import com.likefirst.btos.data.entities.BasicHistory
import com.likefirst.btos.data.entities.Content
import com.likefirst.btos.data.entities.SenderList
import com.likefirst.btos.data.remote.history.response.HistoryBaseResponse
import com.likefirst.btos.data.remote.history.response.HistoryDetailResponse
import com.likefirst.btos.data.remote.history.response.HistorySenderDetailResponse
import com.likefirst.btos.data.remote.history.view.HistoryDetailView
import com.likefirst.btos.data.remote.history.view.HistoryDiaryandLetterView
import com.likefirst.btos.data.remote.history.view.HistorySenderView
import com.likefirst.btos.data.remote.history.view.SenderDetailView
import com.likefirst.btos.presentation.view.history.HistoryBasicRecyclerViewAdapter
import com.likefirst.btos.presentation.view.history.HistoryDetailRecyclerViewAdapter
import com.likefirst.btos.utils.RetrofitInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HistoryService {

    private lateinit var historySenderView : HistorySenderView
    private lateinit var historyDLView: HistoryDiaryandLetterView
    private lateinit var senderDetailView: SenderDetailView
    private lateinit var historyDetailView: HistoryDetailView

    private val HistoryService = retrofit.create(RetrofitInterface::class.java)

    fun setHistorySenderView(historySenderView : HistorySenderView){
        this.historySenderView = historySenderView
    }

    fun sethistoryDiaryView(historyDLView : HistoryDiaryandLetterView){
        this.historyDLView = historyDLView
    }

    fun setSenderDetailView(senderDetailView: SenderDetailView){
        this.senderDetailView = senderDetailView
    }

    fun setHistoryDetailView(historyDetailView: HistoryDetailView){
        this.historyDetailView = historyDetailView
    }

    fun sender(userIdx : Int, pageNum : Int, filtering : String, search :  String?, recyclerViewAdapter : HistoryBasicRecyclerViewAdapter){
        historySenderView.onHistorySenderLoading()

        HistoryService.historyListSender(userIdx, pageNum, filtering, search).enqueue(object :
            Callback<HistoryBaseResponse<BasicHistory<SenderList>>> {
            override fun onResponse(
                call: Call<HistoryBaseResponse<BasicHistory<SenderList>>>,
                response: Response<HistoryBaseResponse<BasicHistory<SenderList>>>,
            ) {
                val senderList: HistoryBaseResponse<BasicHistory<SenderList>> = response.body()!!

                when(senderList.code){
                    1000->historySenderView.onHistorySenderSuccess(senderList.result, senderList.pageInfo, recyclerViewAdapter)
                    else->historySenderView.onHistorySenderFailure(senderList.code,senderList.message, recyclerViewAdapter)
                }
            }
            override fun onFailure(
                call: Call<HistoryBaseResponse<BasicHistory<SenderList>>>,
                t: Throwable,
            ) {
                historySenderView.onHistorySenderFailure(400,"네트워크 오류가 발생했습니다.", recyclerViewAdapter)
            }
        })

    }

    fun diaryletter(userIdx : Int, pageNum : Int, filtering : String, search :  String?, recyclerViewAdapter : HistoryBasicRecyclerViewAdapter){
        historyDLView.onHistoryDiaryLoading()

        HistoryService.historyListDiaryLetter(userIdx, pageNum, filtering, search).enqueue(object :
            Callback<HistoryBaseResponse<BasicHistory<Content>>> {
            override fun onResponse(
                call: Call<HistoryBaseResponse<BasicHistory<Content>>>,
                response: Response<HistoryBaseResponse<BasicHistory<Content>>>,
            ) {
                val DLlist: HistoryBaseResponse<BasicHistory<Content>> = response.body()!!

                when(DLlist.code){
                    1000->historyDLView.onHistoryDiarySuccess(DLlist.result, DLlist.pageInfo, recyclerViewAdapter)
                    else->historyDLView.onHistoryDiaryFailure(DLlist.code,DLlist.message, recyclerViewAdapter)
                }
            }
            override fun onFailure(
                call: Call<HistoryBaseResponse<BasicHistory<Content>>>,
                t: Throwable,
            ) {
                historyDLView.onHistoryDiaryFailure(400,"네트워크 오류가 발생했습니다.", recyclerViewAdapter)
            }
        })
    }

    fun senderDetail(userIdx : Int, senderNickName : String, pageNum : Int, search : String?, recyclerViewAdapter: HistoryBasicRecyclerViewAdapter){
        senderDetailView.onSenderDetailLoading()

        HistoryService.historyListSenderDetail(userIdx, senderNickName, pageNum, search).enqueue(object :
            Callback<HistorySenderDetailResponse> {
            override fun onResponse(
                call: Call<HistorySenderDetailResponse>,
                response: Response<HistorySenderDetailResponse>,
            ) {
                val detailList: HistorySenderDetailResponse = response.body()!!

                when(detailList.code){
                    1000->senderDetailView.onSenderDetailSuccess(detailList.result, detailList.pageInfo, recyclerViewAdapter)
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

    fun historyDetail(userIdx : Int, type : String, typeIdx : Int, recyclerViewAdapter: HistoryDetailRecyclerViewAdapter){
        historyDetailView.onHistoryDetailLoading()

        HistoryService.historyDetailList(userIdx, type, typeIdx).enqueue(object:
            Callback<HistoryDetailResponse>{
            override fun onResponse(
                call: Call<HistoryDetailResponse>,
                response: Response<HistoryDetailResponse>,
            ) {
                val historyList: HistoryDetailResponse = response.body()!!

                when(historyList.code){
                    1000->historyDetailView.onHistoryDetailSuccess(historyList.result, recyclerViewAdapter)
                    else->historyDetailView.onHistoryDetailFailure(historyList.code,historyList.message)
                }
            }

            override fun onFailure(call: Call<HistoryDetailResponse>, t: Throwable) {
                historyDetailView.onHistoryDetailFailure(400,"네트워크 오류가 발생했습니다.")

            }

        })
    }
}