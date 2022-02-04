package com.likefirst.btos.data.remote.viewer.service

import android.util.Log
import com.likefirst.btos.ApplicationClass
import com.likefirst.btos.ApplicationClass.Companion.retrofit
import com.likefirst.btos.data.entities.UserIsSad
import com.likefirst.btos.data.remote.BaseResponse
import com.likefirst.btos.data.remote.users.view.UpdateIsSadView
import com.likefirst.btos.data.remote.viewer.response.ArchiveCalendar
import com.likefirst.btos.data.remote.viewer.view.ArchiveCalendarView
import com.likefirst.btos.utils.RetrofitInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ArchiveCalendarService {
    private lateinit var archiveCalendarView: ArchiveCalendarView
    // Retrofit 객체 생성
    val archiveCalendarService = retrofit.create(RetrofitInterface::class.java)

    fun setArchiveCalendarView(archiveCalendarView: ArchiveCalendarView){
        this.archiveCalendarView = archiveCalendarView
    }

    fun getCalendar(userIdx : Int, date : String, type : String){
        archiveCalendarView.onArchiveCalendarLoading()
        // API 호출
        archiveCalendarService.getCalendar(userIdx, date, type).enqueue(object : Callback<BaseResponse<ArrayList<ArchiveCalendar>>> {
            override fun onResponse(call: Call<BaseResponse<ArrayList<ArchiveCalendar>>>, response: Response<BaseResponse<ArrayList<ArchiveCalendar>>>) {
                val resp = response.body()!!
                when (resp.code){
                    1000 -> archiveCalendarView.onArchiveCalendarSuccess(resp.result)
                    else -> archiveCalendarView.onArchiveCalendarFailure(resp.code)
                }
            }

            override fun onFailure(call: Call<BaseResponse<ArrayList<ArchiveCalendar>>>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }
}