package com.likefirst.btos.data.remote.posting.service

import android.util.Log
import com.likefirst.btos.ApplicationClass.Companion.retrofit
import com.likefirst.btos.data.remote.BaseResponse
import com.likefirst.btos.data.remote.posting.response.MailInfoResponse
import com.likefirst.btos.data.remote.posting.response.MailLetterResponse
import com.likefirst.btos.data.remote.posting.view.MailLetterView
import com.likefirst.btos.utils.RetrofitInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response




class MailLetterService(){

    private lateinit var mailLetterView: MailLetterView
    private val LetterService =retrofit.create(RetrofitInterface::class.java)


    fun setLetterView(mailLetterView: MailLetterView){
        this.mailLetterView=mailLetterView
    }

    fun loadLetter(userId:Int,type:String, idx:Int){
        mailLetterView.onLetterLoading()

        LetterService.loadLetter(userId,type,idx).enqueue(object:Callback<BaseResponse<MailInfoResponse>> {
            override fun onResponse(call: Call<BaseResponse<MailInfoResponse>>, response: Response<BaseResponse<MailInfoResponse>>) {
                val letterResponse: BaseResponse<MailInfoResponse> =response.body()!!
                Log.d("Letter/APIe",  letterResponse.toString())
                Log.d("Letter/API",letterResponse.code.toString())
                when( letterResponse.code){
                    1000->{
                        mailLetterView.onLetterSuccess( letterResponse.result)
                        return
                    }
                    else->   mailLetterView.onLetterFailure( letterResponse.code,  letterResponse.message)
                }

            }

            override fun onFailure(call: Call<BaseResponse<MailInfoResponse>>, t: Throwable) {

            }

        })
    }
}

