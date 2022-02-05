package com.likefirst.btos.data.remote.notify.service

import android.util.Log
import com.likefirst.btos.ApplicationClass
import com.likefirst.btos.data.remote.notify.response.NoticeAPIResponse
import com.likefirst.btos.data.remote.notify.view.NoticeAPIView
import com.likefirst.btos.utils.RetrofitInterface
import com.likefirst.btos.utils.errorDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NoticeService {
    private lateinit var noticeView: NoticeAPIView

    private val NoticeService = ApplicationClass.retrofit.create(RetrofitInterface::class.java)

    fun setNoticeView(noticeView: NoticeAPIView){
        this.noticeView=noticeView
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
}