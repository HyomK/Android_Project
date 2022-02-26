package com.likefirst.btos.data.remote.notify.service

import com.likefirst.btos.ApplicationClass
import com.likefirst.btos.data.entities.firebase.FcmTokenRequest
import com.likefirst.btos.data.remote.BaseResponse
import com.likefirst.btos.data.remote.notify.view.FcmTokenView
import com.likefirst.btos.utils.RetrofitInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FcmTokenService {
    private val fcmTokenService = ApplicationClass.retrofit.create(RetrofitInterface::class.java)

    private lateinit var fcmTokenView: FcmTokenView

    fun setFcmTokenView(fcmTokenView: FcmTokenView){
        this.fcmTokenView=fcmTokenView
    }

    fun postFcmToken(userIdx: Int, fcmToken : String){
        fcmTokenView.onLoadingFcmToken()

        fcmTokenService.postFcmToken(userIdx, fcmToken).enqueue(object:Callback<BaseResponse<String>>{
            override fun onResponse(
                call: Call<BaseResponse<String>>,
                response: Response<BaseResponse<String>>
            ) {
                val resp = response.body()!!
                when(resp.code){
                    1000-> fcmTokenView.onSuccessFcmToken()
                    else->fcmTokenView.onFailureFcmToken(resp.code, resp.message)
                }
            }

            override fun onFailure(call: Call<BaseResponse<String>>, t: Throwable) {

            }
        })
    }

}