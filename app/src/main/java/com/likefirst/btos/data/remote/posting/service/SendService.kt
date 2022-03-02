package com.likefirst.btos.data.remote.posting.service

import com.likefirst.btos.ApplicationClass
import com.likefirst.btos.data.remote.BaseResponse
import com.likefirst.btos.data.remote.posting.response.SendLetterRequest
import com.likefirst.btos.data.remote.posting.response.SendReplyRequest
import com.likefirst.btos.data.remote.posting.response.SendReplyResponse
import com.likefirst.btos.data.remote.posting.view.SendLetterView
import com.likefirst.btos.data.remote.posting.view.SendReplyView
import com.likefirst.btos.utils.RetrofitInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SendService {
    private lateinit var sendLetterView : SendLetterView
    private lateinit var sendReplyView: SendReplyView

    private val sendService = ApplicationClass.retrofit.create(RetrofitInterface::class.java)

    fun setSendLetterView(sendLetterView: SendLetterView){
        this.sendLetterView= sendLetterView
    }
    fun setSendReplyView(sendReplyView: SendReplyView){
        this.sendReplyView=sendReplyView
    }

    fun sendLetter(request : SendLetterRequest){
        sendLetterView.onSendLetterLoading()
        sendService.sendLetter(request).enqueue(object : Callback<BaseResponse<String>>{
            override fun onResponse(
                call: Call<BaseResponse<String>>,
                response: Response<BaseResponse<String>>
            ) {
                val resp = response.body()!!
                when(resp.code){
                    1000->sendLetterView.onSendLetterSuccess()
                    else->sendLetterView.onSendLetterFailure(resp.code, resp.message)
                }
            }
            override fun onFailure(call: Call<BaseResponse<String>>, t: Throwable) {
            }
        })
    }

    fun sendReply(request : SendReplyRequest){
        sendReplyView.onSendReplyLoading()
        sendService.sendReply(request).enqueue(object : Callback<BaseResponse<SendReplyResponse>>{
            override fun onResponse(
                call: Call<BaseResponse<SendReplyResponse>>,
                response: Response<BaseResponse<SendReplyResponse>>
            ) {
                val resp = response.body()!!
                when(resp.code){
                    1000->sendReplyView.onSendReplySuccess(resp.result)
                    else->sendReplyView.onSendReplyFailure(resp.code, resp.message)
                }
            }
            override fun onFailure(call: Call<BaseResponse<SendReplyResponse>>, t: Throwable) {
            }
        })
    }

}