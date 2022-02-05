package com.likefirst.btos.data.remote.posting.service

import android.util.Log
import com.likefirst.btos.ApplicationClass.Companion.retrofit
import com.likefirst.btos.data.remote.posting.view.MailDiaryView
import com.likefirst.btos.data.remote.posting.response.MailDiaryResponse
import com.likefirst.btos.utils.RetrofitInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MailDiaryService {
    private lateinit var mailDiaryView: MailDiaryView

    private val DiaryService = retrofit.create(RetrofitInterface::class.java)

    fun setDiaryView(mailDiaryView: MailDiaryView){
        this.mailDiaryView=mailDiaryView
    }

    fun loadDiary(type:String, userId:String){

        mailDiaryView.onDiaryLoading()

        DiaryService.loadDiary(type,userId).enqueue(object:Callback<MailDiaryResponse>{
            override fun onResponse(call: Call<MailDiaryResponse>, response: Response<MailDiaryResponse>) {
                val diaryResponse:MailDiaryResponse=response.body()!!
                Log.e("Diary/API",  diaryResponse.toString())

                when( diaryResponse.code){
                    1000->mailDiaryView.onDiarySuccess( diaryResponse.result.content)
                    else->mailDiaryView.onDiaryFailure( diaryResponse.code,  diaryResponse.message)
                }
            }

            override fun onFailure(call: Call<MailDiaryResponse>, t: Throwable) {
                mailDiaryView.onDiaryFailure(4000,"데이터베이스 연결에 실패하였습니다.")
                mailDiaryView.onDiaryFailure(6005,"일기 복호화에 실패하였습니다.")
            }

        })
    }
}