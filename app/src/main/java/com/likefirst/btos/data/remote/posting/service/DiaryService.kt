package com.likefirst.btos.data.remote.posting.service

import android.util.Log
import com.likefirst.btos.ApplicationClass.Companion.retrofit
import com.likefirst.btos.data.entities.PostDiaryRequest
import com.likefirst.btos.data.remote.BaseResponse
import com.likefirst.btos.data.remote.posting.response.MailDiaryResponse
import com.likefirst.btos.data.remote.posting.response.PostDiaryResponse
import com.likefirst.btos.data.remote.posting.view.MailDiaryView
import com.likefirst.btos.data.remote.posting.view.PostDiaryView
import com.likefirst.btos.data.remote.posting.view.UpdateDiaryView
import com.likefirst.btos.data.remote.viewer.response.UpdateDiaryRequest
import com.likefirst.btos.utils.RetrofitInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DiaryService (){


    private lateinit var diaryView: MailDiaryView
    private lateinit var postDiaryView: PostDiaryView
    private lateinit var updateDiaryView: UpdateDiaryView


    private val DiaryService = retrofit.create(RetrofitInterface::class.java)

    fun setDiaryView(diaryView: MailDiaryView){
        this.diaryView=diaryView
    }

    fun setPostDiaryView(postDiaryView: PostDiaryView){
        this.postDiaryView = postDiaryView
    }

    fun setUpdateDiaryView(UpdateDiaryView: UpdateDiaryView){
        this.updateDiaryView = UpdateDiaryView
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

    fun loadDiary( userId:Int, type:String, idx:Int){

        diaryView.onDiaryLoading()

        DiaryService.loadDiary(userId,type,idx).enqueue(object:Callback<BaseResponse<MailDiaryResponse>>{
            override fun onResponse(call: Call<BaseResponse<MailDiaryResponse>>, response: Response<BaseResponse<MailDiaryResponse>>) {
                val diaryResponse :BaseResponse<MailDiaryResponse> =response.body()!!
                Log.e("Diary/API",  diaryResponse.toString())

                when( diaryResponse.code){
                    1000->diaryView.onDiarySuccess( diaryResponse.result)
                    else->diaryView.onDiaryFailure( diaryResponse.code,  diaryResponse.message)
                }
            }

            override fun onFailure(call: Call<BaseResponse<MailDiaryResponse>>, t: Throwable) {

            }

        })
    }

    fun updateDiary(updateRequest : UpdateDiaryRequest){
        updateDiaryView.onArchiveUpdateLoading()

        DiaryService.updateDiary(updateRequest).enqueue(object : Callback<BaseResponse<String>> {
            override fun onResponse(
                call: Call<BaseResponse<String>>,
                response: Response<BaseResponse<String>>,
            ) {
                val resp = response.body()!!
                when (resp.code){
                    1000 -> updateDiaryView.onArchiveUpdateSuccess()
                    else -> updateDiaryView.onArchiveUpdateFailure(resp.code)
                }
            }

            override fun onFailure(call: Call<BaseResponse<String>>, t: Throwable) {
                Log.d("ArchiveCalendarService / getCalendar Failure", t.toString())
                updateDiaryView.onArchiveUpdateFailure(400)
            }
        })
    }
}