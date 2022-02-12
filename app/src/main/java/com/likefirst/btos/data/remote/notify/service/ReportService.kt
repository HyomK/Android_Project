package com.likefirst.btos.data.remote.notify.service

import android.util.Log
import com.likefirst.btos.ApplicationClass
import com.likefirst.btos.data.remote.BaseResponse
import com.likefirst.btos.data.remote.notify.response.NoticeAPIResponse
import com.likefirst.btos.data.remote.notify.response.Report
import com.likefirst.btos.data.remote.notify.response.ReportResponse
import com.likefirst.btos.data.remote.notify.view.NoticeAPIView
import com.likefirst.btos.data.remote.notify.view.ReportView
import com.likefirst.btos.utils.RetrofitInterface
import com.likefirst.btos.utils.errorDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ReportService {
    private lateinit var reportView: ReportView

    private val ReportService = ApplicationClass.retrofit.create(RetrofitInterface::class.java)

    fun setReportView(reportView: ReportView){
        this.reportView=reportView
    }

    fun sendReport(request : Report){
        ReportService .sendReport(request).enqueue(object: Callback<BaseResponse<ReportResponse>> {
            override fun onResponse(call: Call<BaseResponse<ReportResponse>>, response: Response<BaseResponse<ReportResponse>>) {
                val reportResponse : BaseResponse<ReportResponse> =response.body()!!
                Log.e("ReportAPI",response.toString())
                when( reportResponse.code){
                    1000->reportView.onReportSuccess(  reportResponse.result)
                    else-> reportView.onReportFailure(  reportResponse.code,   reportResponse.message)
                }
            }

            override fun onFailure(call: Call<BaseResponse<ReportResponse>>, t: Throwable) {

            }
        })
    }
}