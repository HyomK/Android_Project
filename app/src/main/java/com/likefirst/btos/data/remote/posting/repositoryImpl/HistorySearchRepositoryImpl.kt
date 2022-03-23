package com.likefirst.btos.data.remote.posting.repositoryImpl

import com.likefirst.btos.data.entities.BasicHistory
import com.likefirst.btos.data.entities.Content
import com.likefirst.btos.data.entities.SenderList
import com.likefirst.btos.data.remote.history.response.HistoryBaseResponse
import com.likefirst.btos.data.remote.history.response.HistoryDetailResponse
import com.likefirst.btos.data.remote.history.response.HistorySenderDetailResponse
import com.likefirst.btos.data.remote.posting.dataSource.HistoryRemoteDataSource
import com.likefirst.btos.data.remote.posting.repository.HistorySearchRepository
import kotlinx.coroutines.delay
import retrofit2.Response
import javax.inject.Inject

class HistorySearchRepositoryImpl @Inject constructor(private val remoteDataSource: HistoryRemoteDataSource) :HistorySearchRepository{
    override suspend fun historyListSender(
        userIdx: Int,
        pageNum: Int,
        filtering: String,
        search: String?,
    ): Response<HistoryBaseResponse<BasicHistory<SenderList>>> {
        return remoteDataSource.historyListSender(userIdx,pageNum,filtering, search)
    }

    override suspend fun historyListDiaryLetter(
        userIdx: Int,
        pageNum: Int,
        filtering: String,
        search: String?,
    ): Response<HistoryBaseResponse<BasicHistory<Content>>> {
        delay(100)
        return remoteDataSource.historyListDiaryLetter(userIdx, pageNum, filtering, search)
    }

    override suspend fun historyListSenderDetail(
        userIdx: Int,
        senderNickName: String,
        pageNum: Int,
        search: String?,
    ): Response<HistorySenderDetailResponse> {
        delay(100)
       return remoteDataSource.historyListSenderDetail(userIdx, senderNickName, pageNum, search)
    }

    override suspend fun historyDetailList(
        userIdx: Int,
        type: String,
        typeIdx: Int,
    ): Response<HistoryDetailResponse> {
        return remoteDataSource.historyDetailList(userIdx, type, typeIdx)
    }
}