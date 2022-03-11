package com.likefirst.btos.data.remote.notify.service

import android.util.Log
import com.likefirst.btos.ApplicationClass
import com.likefirst.btos.data.remote.BaseResponse
import com.likefirst.btos.data.remote.notify.response.NoticeAPIResponse
import com.likefirst.btos.data.remote.notify.view.NoticeAPIView
import com.likefirst.btos.data.remote.notify.view.SystemPushAlarmView
import com.likefirst.btos.utils.RetrofitInterface
import com.likefirst.btos.utils.errorDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NoticeService {
    private lateinit var noticeView: NoticeAPIView
    private lateinit var systemPushView : SystemPushAlarmView

    private val NoticeService = ApplicationClass.retrofit.create(RetrofitInterface::class.java)

    fun setNoticeView(noticeView: NoticeAPIView){
        this.noticeView=noticeView
    }

    fun setSystemPushView(systemPushView : SystemPushAlarmView){
        this.systemPushView = systemPushView
    }

    fun loadNotice(){

        NoticeService.loadNotice().enqueue(object: Callback<NoticeAPIResponse> {
            override fun onResponse(call: Call<NoticeAPIResponse>, response: Response<NoticeAPIResponse>) {
                val noticeResponse: NoticeAPIResponse? =response.body()
                Log.e("Notice/API",  noticeResponse.toString())
                if(noticeResponse==null){
                    noticeView.onNoticeAPIError(errorDialog())
                    return
                }
                when( noticeResponse.code){
                    1000-> noticeView.onNoticeAPISuccess( noticeResponse.result)
                    else-> noticeView.onNoticeAPIFailure( noticeResponse.code,  noticeResponse.message)
                }
            }

            override fun onFailure(call: Call<NoticeAPIResponse>, t: Throwable) {

            }
        })
    }

    fun loadSystemPushAlarm(userIdx : Int){
        NoticeService.getSystemPushAlarm(userIdx).enqueue(object : Callback<BaseResponse<String>>{
            override fun onResponse(
                call: Call<BaseResponse<String>>,
                response: Response<BaseResponse<String>>,
            ) {
                val resp = response.body()!!
                when(resp.code){
                    1000 -> systemPushView.onSystemPushAlarmSuccess()
                    else -> systemPushView.onSystemPushAlarmFailure(resp.code)
                }
            }

            override fun onFailure(call: Call<BaseResponse<String>>, t: Throwable) {
                Log.d("Err loadSystemPushAlarm", t.toString())
            }

        })
    }
}