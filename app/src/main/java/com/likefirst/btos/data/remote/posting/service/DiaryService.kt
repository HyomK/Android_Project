package com.likefirst.btos.data.remote.posting.service

import android.util.Log
import com.likefirst.btos.ApplicationClass.Companion.retrofit
import com.likefirst.btos.data.entities.PostDiaryRequest
import com.likefirst.btos.data.remote.BaseResponse
import com.likefirst.btos.data.remote.posting.response.DiaryResponse
import com.likefirst.btos.data.remote.posting.response.PostDiaryResponse
import com.likefirst.btos.data.remote.posting.view.DiaryView
import com.likefirst.btos.data.remote.posting.view.PostDiaryView
import com.likefirst.btos.utils.RetrofitInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DiaryService {
    private lateinit var diaryView: DiaryView
    private lateinit var postDiaryView: PostDiaryView

    private val DiaryService = retrofit.create(RetrofitInterface::class.java)

    fun setDiaryView(diaryView: DiaryView){
        this.diaryView=diaryView
    }

    fun setPostDiaryView(postDiaryView: PostDiaryView){
        this.postDiaryView = postDiaryView
    }

    fun postDiary(postDiaryRequest: PostDiaryRequest){
        postDiaryView.onDiaryPostLoading()

        DiaryService.postDiary(postDiaryRequest).enqueue(object :Callback<BaseResponse<PostDiaryResponse>>{
            override fun onResponse(call: Call<BaseResponse<PostDiaryResponse>>, response: Response<BaseResponse<PostDiaryResponse>>, ) {
                val resp = response.body()!!
                Log.d("debug", resp.toString())
                when (resp.code){
                    1000 -> postDiaryView.onDiaryPostSuccess()
                    else -> postDiaryView.onDiaryPostFailure(resp.code)
                }
            }

            override fun onFailure(call: Call<BaseResponse<PostDiaryResponse>>, t: Throwable) {
                // Network Error
                postDiaryView.onDiaryPostFailure(400)
            }

        })
    }

    fun loadDiary(type:String, userId:String){

        diaryView.onDiaryLoading()

        DiaryService.loadDiary(type,userId).enqueue(object:Callback<DiaryResponse>{
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
                diaryView.onDiaryFailure(6005,"일기 복호화에 실패하였습니다.")
            }

        })
    }
}