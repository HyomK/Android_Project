package com.likefirst.btos.data.remote.posting.service

import android.util.Log
import com.likefirst.btos.ApplicationClass
import com.likefirst.btos.data.remote.response.ReplyResponse
import com.likefirst.btos.data.remote.posting.view.ReplyView
import com.likefirst.btos.utils.RetrofitInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ReplyService {
    private lateinit var replyView: ReplyView

    private val ReplyService = ApplicationClass.retrofit.create(RetrofitInterface::class.java)

    fun setDiaryView( replyView: ReplyView){
        this.replyView=replyView
    }

    fun loadReply(type:String,userId:String){

        replyView.onReplyLoading()

        ReplyService. loadReply(type,userId).enqueue(object: Callback<ReplyResponse> {
            override fun onResponse(call: Call<ReplyResponse>, response: Response<ReplyResponse>) {
                val replyResponse: ReplyResponse =response.body()!!
                Log.e("Reply/API", replyResponse.toString())

                when(replyResponse.code){
                    1000->replyView.onReplySuccess( replyResponse.result.content)
                    else->replyView.onReplyFailure( replyResponse.code, replyResponse.message)
                }
            }

            override fun onFailure(call: Call<ReplyResponse>, t: Throwable) {
                replyView.onReplyFailure(4000,"데이터베이스 연결에 실패하였습니다.")
                replyView.onReplyFailure(6006,"일기 복호화에 실패하였습니다.")
            }

        })
    }
}