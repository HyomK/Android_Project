package com.likefirst.btos.data.remote.posting.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.google.gson.Gson
import com.likefirst.btos.ApplicationClass
import com.likefirst.btos.data.local.PlantRepository
import com.likefirst.btos.data.remote.BaseResponse
import com.likefirst.btos.data.remote.posting.response.MailInfoResponse
import com.likefirst.btos.data.remote.posting.response.Mailbox
import com.likefirst.btos.data.remote.posting.response.MailboxResponse
import com.likefirst.btos.data.remote.posting.service.MailboxService
import com.likefirst.btos.data.remote.posting.view.MailDiaryView
import com.likefirst.btos.data.remote.posting.view.MailLetterView
import com.likefirst.btos.data.remote.posting.view.MailReplyView
import com.likefirst.btos.data.remote.posting.view.MailboxView
import com.likefirst.btos.utils.RetrofitInterface
import com.likefirst.btos.utils.getUserIdx
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MailViewModel constructor( private val repository: MailboxRepository): ViewModel() {
    val mailList = MutableLiveData<ArrayList<Mailbox>>()
    val gson = Gson()

    fun loadMailList(view : MailboxView, userIdx : Int) {
       viewModelScope.launch {
           val response = repository.loadMailboxList(userIdx)
           if(response.isSuccessful){
               view.onMailboxSuccess(response.body()?.result!!)
               mailList.postValue(response.body()!!.result)
           }else{
               view.onMailboxFailure(response.body()!!.code, response.body()!!.message)
           }
       }
    }

    fun loadDiary(view: MailDiaryView, userIdx: Int , typeIdx: Int){
       viewModelScope.launch {
           /*view.onDiaryLoading()
           val response = repository.loadDiary(userIdx, typeIdx)
           if (response.) {
               view.onDiarySuccess(response.body()!!.result)
           } else {
               view.onDiaryFailure(response.body()!!.code, response.body()!!.message)
           }*/
            repository.loadDiary(userIdx,typeIdx).enqueue(object: Callback<BaseResponse<MailInfoResponse>> {
               override fun onResponse(
                   call: Call<BaseResponse<MailInfoResponse>>,
                   response: Response<BaseResponse<MailInfoResponse>>
               ) {
                   val mailResponse: BaseResponse<MailInfoResponse> =response.body()!!
                   when(mailResponse.code){
                       1000->{
                           view.onDiarySuccess(mailResponse.result)
                       }
                       else->view.onDiaryFailure(mailResponse.code,mailResponse.message)
                   }
               }
               override fun onFailure(call: Call<BaseResponse<MailInfoResponse>>, t: Throwable) {

               }
           })
       }

    }

    fun loadLetter(view : MailLetterView, userIdx: Int , typeIdx: Int){
        viewModelScope.launch {
          /*  view.onLetterLoading()
            val response = repository.loadLetter(userIdx, typeIdx)
            if (response.body()?.code ==1000) {
                view.onLetterSuccess(response.body()?.result!!)
            } else {
                view.onLetterFailure(response.body()!!.code, response.body()!!.message)
            }*/

            repository.loadLetter(userIdx,typeIdx).enqueue(object: Callback<BaseResponse<MailInfoResponse>> {
                override fun onResponse(
                    call: Call<BaseResponse<MailInfoResponse>>,
                    response: Response<BaseResponse<MailInfoResponse>>
                ) {
                    val mailResponse: BaseResponse<MailInfoResponse> =response.body()!!
                    when(mailResponse.code){
                        1000->{
                            view.onLetterSuccess(mailResponse.result)
                        }
                        else->view.onLetterFailure(mailResponse.code,mailResponse.message)
                    }
                }
                override fun onFailure(call: Call<BaseResponse<MailInfoResponse>>, t: Throwable) {

                }
            })
        }

    }

    fun loadReply(view : MailReplyView, userIdx:Int, typeIdx:Int){
        viewModelScope.launch {
           /* view.onReplyLoading()
            val response = repository.loadReply(userIdx, typeIdx)
            if (response.body()?.code ==1000) {
                view.onReplySuccess(response.body()?.result!!)
            } else {
                view.onReplyFailure(response.body()!!.code, response.body()!!.message)
            }*/
            view.onReplyLoading()
            repository.loadReply(userIdx,typeIdx).enqueue(object: Callback<BaseResponse<MailInfoResponse>> {
                override fun onResponse(
                    call: Call<BaseResponse<MailInfoResponse>>,
                    response: Response<BaseResponse<MailInfoResponse>>
                ) {
                    val mailResponse: BaseResponse<MailInfoResponse> =response.body()!!
                    when(mailResponse.code){
                        1000->{
                            view.onReplySuccess(mailResponse.result)
                        }
                        else->view.onReplyFailure(mailResponse.code,mailResponse.message)
                    }
                }
                override fun onFailure(call: Call<BaseResponse<MailInfoResponse>>, t: Throwable) {

                }
            })
        }
    }

}