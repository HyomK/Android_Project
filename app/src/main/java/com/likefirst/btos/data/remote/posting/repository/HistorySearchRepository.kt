package com.likefirst.btos.data.remote.posting.repository

import com.likefirst.btos.data.entities.BasicHistory
import com.likefirst.btos.data.entities.Content
import com.likefirst.btos.data.entities.SenderList
import com.likefirst.btos.data.remote.history.response.HistoryBaseResponse
import com.likefirst.btos.data.remote.history.response.HistoryDetailResponse
import com.likefirst.btos.data.remote.history.response.HistorySenderDetailResponse
import retrofit2.Response

interface HistorySearchRepository {
    suspend fun historyListSender(userIdx : Int,
                                  pageNum : Int,
                                  filtering: String,
                                  search: String?): Response<HistoryBaseResponse<BasicHistory<SenderList>>>
    suspend fun historyListDiaryLetter( userIdx : Int,
                                        pageNum : Int,
                                        filtering: String,
                                        search: String?): Response<HistoryBaseResponse<BasicHistory<Content>>>
    suspend fun  historyListSenderDetail( userIdx : Int,
                                          senderNickName : String,
                                          pageNum : Int,
                                          search: String?): Response<HistorySenderDetailResponse>
    suspend fun historyDetailList( userIdx : Int,
                                   type : String,
                                   typeIdx : Int,): Response<HistoryDetailResponse>
}