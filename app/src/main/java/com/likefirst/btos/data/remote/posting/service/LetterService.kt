package com.likefirst.btos.data.remote.posting.service

import android.util.Log
import com.likefirst.btos.ApplicationClass.Companion.retrofit
import com.likefirst.btos.data.remote.posting.response.LetterResponse
import com.likefirst.btos.data.remote.posting.view.LetterView
import com.likefirst.btos.utils.RetrofitInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response




class LetterService{

    private lateinit var letterView: LetterView

    private val LetterService =retrofit.create(RetrofitInterface::class.java)

    fun setLetterView(letterView: LetterView){
        this.letterView=letterView
    }

    fun loadLetter(type:String,userId:String){
        letterView.onLetterLoading()

        LetterService.loadLetter(type,userId).enqueue(object:Callback<LetterResponse> {
            override fun onResponse(call: Call<LetterResponse>, response: Response<LetterResponse>) {
                val letterResponse: LetterResponse =response.body()!!
                Log.d("Letter/APIcode",  letterResponse.toString())
                Log.d("Letter/API",letterResponse.code.toString())
                when( letterResponse.code){
                    1000->{
                        letterView.onLetterSuccess( letterResponse.result.content)
                        return
                    }

                    else->   letterView.onLetterFailure( letterResponse.code,  letterResponse.message)
                }

            }

            override fun onFailure(call: Call<LetterResponse>, t: Throwable) {

                letterView.onLetterFailure(4000,"데이터베이스 연결에 실패하였습니다.")
                letterView.onLetterFailure(6006,"일기 복호화에 실패하였습니다.")
            }

        })
    }
}

