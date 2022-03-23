package com.likefirst.btos.data.remote.posting.dataSource

import com.likefirst.btos.data.entities.BasicHistory
import com.likefirst.btos.data.entities.Content
import com.likefirst.btos.data.entities.SenderList
import com.likefirst.btos.data.remote.history.response.HistoryBaseResponse
import com.likefirst.btos.data.remote.history.response.HistoryDetailResponse
import com.likefirst.btos.data.remote.history.response.HistorySenderDetailResponse
import com.likefirst.btos.data.remote.posting.HistoryApi
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import javax.inject.Inject

class HistoryRemoteDataSource @Inject constructor(val historyApi : HistoryApi) {
    suspend fun historyListSender(userIdx : Int,
                                  pageNum : Int,
                                  filtering: String,
                                  search: String?)
    : Response<HistoryBaseResponse<BasicHistory<SenderList>>>{
        return historyApi.historyListSender(userIdx, pageNum, filtering, search)
    }

    suspend fun historyListDiaryLetter(
        userIdx : Int,
        pageNum : Int,
        filtering: String,
        search: String?
    ) : Response<HistoryBaseResponse<BasicHistory<Content>>>{
        return historyApi.historyListDiaryLetter(userIdx, pageNum, filtering, search)
    }


    suspend fun historyListSenderDetail(
        userIdx : Int,
        senderNickName : String,
        pageNum : Int,
        search: String?
    ) : Response<HistorySenderDetailResponse>{
        return historyApi.historyListSenderDetail(userIdx,senderNickName,pageNum,search)
    }

    suspend fun historyDetailList(
        userIdx : Int,
        type : String,
        typeIdx : Int,
    ) : Response<HistoryDetailResponse>{
        return historyApi.historyDetailList(userIdx,type,typeIdx)
    }
}