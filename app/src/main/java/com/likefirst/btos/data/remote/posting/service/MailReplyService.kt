package com.likefirst.btos.data.remote.posting.service

import android.util.Log
import com.likefirst.btos.ApplicationClass
import com.likefirst.btos.data.remote.BaseResponse
import com.likefirst.btos.data.remote.posting.response.MailInfoResponse
import com.likefirst.btos.data.remote.posting.response.MailReplyResponse
import com.likefirst.btos.data.remote.posting.view.MailReplyView
import com.likefirst.btos.utils.RetrofitInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MailReplyService() {
    private lateinit var mailReplyView: MailReplyView
    private val ReplyService = ApplicationClass.retrofit.create(RetrofitInterface::class.java)


    fun setReplyView(mailReplyView: MailReplyView){
        this.mailReplyView=mailReplyView
    }

    fun loadReply(userId:Int, type:String, idx:Int){


        ReplyService. loadReply(userId, type, idx).enqueue(object: Callback<BaseResponse<MailInfoResponse>>{
            override fun onResponse(call: Call<BaseResponse<MailInfoResponse>>, response: Response<BaseResponse<MailInfoResponse>>) {
                val replyResponse :BaseResponse<MailInfoResponse> =response.body()!!
                Log.e("Reply/API", replyResponse.toString())

                when(replyResponse.code){
                    1000->mailReplyView.onReplySuccess( replyResponse.result)
                    else->mailReplyView.onReplyFailure( replyResponse.code, replyResponse.message)
                }
            }

            override fun onFailure(call: Call<BaseResponse<MailInfoResponse>>, t: Throwable) {
                mailReplyView.onReplyFailure(4000,"데이터베이스 연결에 실패하였습니다.")
                mailReplyView.onReplyFailure(6006,"일기 복호화에 실패하였습니다.")
            }

        })
    }
}