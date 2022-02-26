package com.likefirst.btos.data.remote.notify.service

import com.likefirst.btos.ApplicationClass
import com.likefirst.btos.data.remote.BaseResponse
import com.likefirst.btos.data.remote.notify.response.Alarm
import com.likefirst.btos.data.remote.notify.response.AlarmInfo
import com.likefirst.btos.data.remote.notify.view.AlarmInfoView
import com.likefirst.btos.data.remote.notify.view.AlarmListView
import com.likefirst.btos.utils.RetrofitInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AlarmService {
    private val AlarmService= ApplicationClass.retrofit.create(RetrofitInterface::class.java)

    private lateinit var alarmListView: AlarmListView
    private lateinit var alarmInfoView: AlarmInfoView

    fun setAlarmListView(alarmListView: AlarmListView){
        this.alarmListView=alarmListView
    }
    fun setAlarmInfoView(alarmInfoView: AlarmInfoView){
        this.alarmInfoView=alarmInfoView
    }

    fun getAlarmList(userIdx : Int){
        AlarmService.getAlarmList(userIdx).enqueue(object: Callback<BaseResponse<ArrayList<Alarm>>>{
            override fun onResponse(
                call: Call<BaseResponse<ArrayList<Alarm>>>,
                response: Response<BaseResponse<ArrayList<Alarm>>>
            ) {
                val alarmListResponse = response.body()!!
                when(alarmListResponse.code){
                    1000->alarmListView.onGetAlarmListSuccess(alarmListResponse.result)
                    else->alarmListView.onGetAlarmListFailure(alarmListResponse.code , alarmListResponse.message)
                }
            }
            override fun onFailure(call: Call<BaseResponse<ArrayList<Alarm>>>, t: Throwable) {

            }
        })
    }

    fun getAlarmInfo(alarmIdx:Int ,userIdx: Int){
        AlarmService.getAlarmInfo(alarmIdx = alarmIdx ,userIdx = userIdx).enqueue(object:Callback<BaseResponse<AlarmInfo>>{
            override fun onResponse(
                call: Call<BaseResponse<AlarmInfo>>,
                response: Response<BaseResponse<AlarmInfo>>
            ) {
                val alarmInfoResponse = response. body()!!
                when(alarmInfoResponse.code){
                    1000->alarmInfoView.onGetAlarmInfoViewSuccess(alarmInfoResponse.result)
                    else->alarmInfoView.onGetAlarmInfoFailure(alarmInfoResponse.code, alarmInfoResponse.message)
                }
            }

            override fun onFailure(call: Call<BaseResponse<AlarmInfo>>, t: Throwable) {

            }
        })
    }
}