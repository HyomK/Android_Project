package com.likefirst.btos.data.remote.service

import android.util.Log
import com.likefirst.btos.ApplicationClass.Companion.retrofit
import com.likefirst.btos.data.remote.response.DiaryResponse
import com.likefirst.btos.data.remote.view.DiaryView
import com.likefirst.btos.utils.RetrofitInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DiaryService {
    private lateinit var diaryView: DiaryView

    private val DiaryService = retrofit.create(RetrofitInterface::class.java)

    fun setDiaryView(diaryView: DiaryView){
        this.diaryView=diaryView
    }

    fun loadDiary(userId:String){

        diaryView.onDiaryLoading()

        DiaryService.loadDiary(userId).enqueue(object:Callback<DiaryResponse>{
            override fun onResponse(call: Call<DiaryResponse>, response: Response<DiaryResponse>) {
                val diaryResponse:DiaryResponse=response.body()!!
                Log.e("Diary/API",  diaryResponse.toString())

                when( diaryResponse.code){
                    1000->diaryView.onDiarySuccess( diaryResponse.result.content)
                    else->diaryView.onDiaryFailure( diaryResponse.code,  diaryResponse.message)
                }
            }

            override fun onFailure(call: Call<DiaryResponse>, t: Throwable) {
               diaryView.onDiaryFailure(4000,"데이터베이스 연결에 실패하였습니다.")
                diaryView.onDiaryFailure(6006,"일기 복호화에 실패하였습니다.")
            }

        })
    }
}