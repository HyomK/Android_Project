package com.likefirst.btos.data.remote.viewer.service

import android.util.Log
import com.likefirst.btos.ApplicationClass.Companion.retrofit
import com.likefirst.btos.data.remote.BaseResponse
import com.likefirst.btos.data.remote.viewer.response.ArchiveDiaryResult
import com.likefirst.btos.data.remote.viewer.view.ArchiveDiaryView
import com.likefirst.btos.utils.RetrofitInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ArchiveDiaryService {
    private lateinit var archiveDiaryView: ArchiveDiaryView
    // Retrofit 객체 생성
    val archiveDiaryService = retrofit.create(RetrofitInterface::class.java)

    fun setArchiveCalendarView(archiveDiaryView: ArchiveDiaryView){
        this.archiveDiaryView = archiveDiaryView
    }

    fun getDiary(diaryIdx : Int){
        archiveDiaryView.onArchiveDiaryLoading()

        archiveDiaryService.getDiary(diaryIdx).enqueue(object : Callback<BaseResponse<ArchiveDiaryResult>> {
            override fun onResponse(
                call: Call<BaseResponse<ArchiveDiaryResult>>,
                response: Response<BaseResponse<ArchiveDiaryResult>>,
            ) {
                val resp = response.body()!!
                when (resp.code){
                    1000 -> archiveDiaryView.onArchiveDiarySuccess(resp.result)
                    else -> archiveDiaryView.onArchiveDiaryFailure(resp.code)
                }
            }

            override fun onFailure(call: Call<BaseResponse<ArchiveDiaryResult>>, t: Throwable) {
                Log.d("ArchiveCalendarService / getCalendar Failure", t.toString())
                archiveDiaryView.onArchiveDiaryFailure(400)
            }
        })
    }
}