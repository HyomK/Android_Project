package com.likefirst.btos.ui.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.likefirst.btos.data.entities.BasicHistory
import com.likefirst.btos.data.entities.Content
import com.likefirst.btos.data.entities.SenderList
import com.likefirst.btos.data.remote.history.response.HistoryBaseResponse
import com.likefirst.btos.data.remote.history.response.HistoryDetailResponse
import com.likefirst.btos.data.remote.history.response.HistorySenderDetailResponse
import com.likefirst.btos.data.remote.posting.repository.HistorySearchRepository
import com.likefirst.btos.domain.ApiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class HistorySearchViewModel  @Inject constructor(private val repository :HistorySearchRepository):ViewModel() {
    private val _sender_li_res = MutableLiveData<ApiResult<HistoryBaseResponse<BasicHistory<SenderList>>>>()
    val sender_li : LiveData<ApiResult<HistoryBaseResponse<BasicHistory<SenderList>>>>
        get() = _sender_li_res

    private val _dl_li_res = MutableLiveData<ApiResult<HistoryBaseResponse<BasicHistory<Content>>>>()
    val dl_li : LiveData<ApiResult<HistoryBaseResponse<BasicHistory<Content>>>>
        get() = _dl_li_res

    private val _sender_detail_res = MutableLiveData<ApiResult<HistorySenderDetailResponse>>()
    val sender_detail : LiveData<ApiResult<HistorySenderDetailResponse>>
        get() = _sender_detail_res

    private val _dl_detail_res = MutableLiveData<ApiResult<HistoryDetailResponse>>()
    val dl_detail : LiveData<ApiResult<HistoryDetailResponse>>
        get() = _dl_detail_res

    private val SEARCH_TIMEOUT = 500L
    /*
    Channel.CONFLATED는 ConflatedChannel를 생성
    ConflatedChannel:
        보내진 element 중에서 하나의 element만 버퍼링하므로서
        Receiver가 항상 최근에 보내진 element를 가져올 수 있도록함
     */

    val queryChannel = MutableStateFlow(HistoryRequest(null,null,null,null))
    fun setSearchQuery(request: HistoryRequest) { queryChannel.value = request}

    fun searchSender(req : HistoryRequest) : Flow<HistoryRequest> = flow{
        if(req !=null){
            Log.e("WATCHER-FLOW",req.toString())
            getSenderList(req)
        }
    }

    @ExperimentalCoroutinesApi
    @FlowPreview
    val searchResult = queryChannel
        .debounce(SEARCH_TIMEOUT)
        .flatMapLatest {
            withContext(Dispatchers.IO){
               searchSender(it)
            }
        }.catch { e: Throwable ->
            // 에러 핸들링은 여기서!
            e.printStackTrace()
        }


    fun getSenderList( req: HistoryRequest)= CoroutineScope(Dispatchers.IO).launch {

        _sender_li_res.postValue(ApiResult.loading())
        repository.historyListSender(req.userIdx!!, req.pageNum!!, req.filtering!!, req.search).let{
            if(it.isSuccessful){
                if(it.body()?.code==1000) _sender_li_res.postValue(ApiResult.success(it.code(),it.body()))
                else  _sender_li_res.postValue(ApiResult.error(it.code(),it.message()))
            }else{
                _sender_li_res.postValue(ApiResult.error(it.code(),it.message()))
            }
        }
    }

    fun getDiaryLetterList( req: HistoryRequest)= CoroutineScope(Dispatchers.IO).launch {

        _dl_li_res.postValue(ApiResult.loading())
        repository.historyListDiaryLetter(req.userIdx!!, req.pageNum!!, req.filtering!!, req.search).let{
            if(it.isSuccessful){
                if(it.body()?.code==1000)_dl_li_res.postValue(ApiResult.success(it.code(),it.body()))
                else _dl_li_res.postValue(ApiResult.error(it.code(),it.message()))
            }else{
                _dl_li_res.postValue(ApiResult.error(it.code(),it.message()))
            }
        }
    }

    fun getSenderDetailList( userIdx: Int,
                             senderNickName: String,
                             pageNum: Int,
                             search: String?)= CoroutineScope(Dispatchers.IO).launch {

        _sender_detail_res.postValue(ApiResult.loading())

        repository.historyListSenderDetail(userIdx, senderNickName,pageNum, search).let{
            if(it.isSuccessful){
                _sender_detail_res.postValue(ApiResult.success(it.code(),it.body()))
            }else{
                _sender_detail_res.postValue(ApiResult.error(it.code(),it.message()))
            }
        }
    }


    fun getDetailList( userIdx: Int,
                            type: String,
                            typeIdx: Int,)= CoroutineScope(Dispatchers.IO).launch {

        _dl_detail_res.postValue(ApiResult.loading())
        repository. historyDetailList(userIdx, type,typeIdx).let{
            if(it.isSuccessful){
                _dl_detail_res.postValue(ApiResult.success(it.code(),it.body()))
            }else{
                _dl_detail_res.postValue(ApiResult.error(it.code(),it.message()))
            }
        }
    }


}

data class HistoryRequest(
    val userIdx: Int?,
    val pageNum: Int?,
    val filtering: String?,
    val search: String?
)