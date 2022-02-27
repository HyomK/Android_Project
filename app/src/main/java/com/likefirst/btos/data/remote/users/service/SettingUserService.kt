package com.likefirst.btos.data.remote.users.service

import android.util.Log
import com.likefirst.btos.ApplicationClass.Companion.retrofit
import com.likefirst.btos.data.entities.*
import com.likefirst.btos.data.remote.BaseResponse
import com.likefirst.btos.data.remote.users.view.SetSettingUserView
import com.likefirst.btos.utils.RetrofitInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SettingUserService {

    private lateinit var setSettingUserView: SetSettingUserView

    val SettingUserService = retrofit.create(RetrofitInterface::class.java)

    fun setSettingUserView(setSettingUserView : SetSettingUserView){
        this.setSettingUserView = setSettingUserView
    }

    fun setName(userIdx : Int, nickname : UserName){
        setSettingUserView.onSetSettingUserViewLoading()
        SettingUserService.setName(userIdx, nickname).enqueue(object: Callback<BaseResponse<String>> {
            override fun onResponse(call: Call<BaseResponse<String>>, response: Response<BaseResponse<String>>) {
                baseResponseSuccess(response)
            }
            override fun onFailure(call: Call<BaseResponse<String>>, t: Throwable) {
                setSettingUserView.onSetSettingUserViewFailure(4000, "데이터베이스 연결에 실패하였습니다.")
            }
        })
    }

    fun setBirth(userIdx : Int, birth : UserBirth){
        setSettingUserView.onSetSettingUserViewLoading()
        Log.d("userBirth", birth.toString())
        SettingUserService.setBirth(userIdx, birth).enqueue(object: Callback<BaseResponse<String>> {
            override fun onResponse(call: Call<BaseResponse<String>>, response: Response<BaseResponse<String>>) {
                baseResponseSuccess(response)
            }
            override fun onFailure(call: Call<BaseResponse<String>>, t: Throwable) {
                setSettingUserView.onSetSettingUserViewFailure(4000, "데이터베이스 연결에 실패하였습니다.")
            }

        })
    }

        fun setNotificationOther(userIdx : Int, recOthers : UserOther){
        setSettingUserView.onSetSettingUserViewLoading()
        SettingUserService.setNotificationOther(userIdx, recOthers).enqueue(object: Callback<BaseResponse<String>> {
            override fun onResponse(call: Call<BaseResponse<String>>, response: Response<BaseResponse<String>>) {
                baseResponseSuccess(response)
            }
            override fun onFailure(call: Call<BaseResponse<String>>, t: Throwable) {
                setSettingUserView.onSetSettingUserViewFailure(4000, "데이터베이스 연결에 실패하였습니다.")
            }

        })
    }

    fun setNotificationAge(userIdx : Int, recSimilarAge : UserAge){
        setSettingUserView.onSetSettingUserViewLoading()
        SettingUserService.setNotificationAge(userIdx, recSimilarAge).enqueue(object: Callback<BaseResponse<String>> {
            override fun onResponse(call: Call<BaseResponse<String>>, response: Response<BaseResponse<String>>) {
                baseResponseSuccess(response)
            }
            override fun onFailure(call: Call<BaseResponse<String>>, t: Throwable) {
                setSettingUserView.onSetSettingUserViewFailure(4000, "데이터베이스 연결에 실패하였습니다.")
            }
        })
    }

    fun setPushAlarm(userIdx : Int, pushAlarm : UserPush){
        setSettingUserView.onSetSettingUserViewLoading()
        SettingUserService.setPushAlarm(userIdx, pushAlarm).enqueue(object: Callback<BaseResponse<String>> {
            override fun onResponse(call: Call<BaseResponse<String>>, response: Response<BaseResponse<String>>) {
                baseResponseSuccess(response)
            }
            override fun onFailure(call: Call<BaseResponse<String>>, t: Throwable) {
                setSettingUserView.onSetSettingUserViewFailure(4000, "데이터베이스 연결에 실패하였습니다.")
            }
        })
    }

    fun setFont(userIdx : Int, fontIdx : UserFont){
        setSettingUserView.onSetSettingUserViewLoading()
        SettingUserService.setFont(userIdx, fontIdx).enqueue(object: Callback<BaseResponse<String>> {
            override fun onResponse(call: Call<BaseResponse<String>>, response: Response<BaseResponse<String>>) {
                baseResponseSuccess(response)
            }
            override fun onFailure(call: Call<BaseResponse<String>>, t: Throwable) {
                setSettingUserView.onSetSettingUserViewFailure(4000, "데이터베이스 연결에 실패하였습니다.")
            }
        })
    }


    fun leave(userIdx : Int, status : UserLeave){
        setSettingUserView.onSetSettingUserViewLoading()
        SettingUserService.leave(userIdx, status).enqueue(object: Callback<BaseResponse<String>> {
            override fun onResponse(call: Call<BaseResponse<String>>, response: Response<BaseResponse<String>>) {
                baseResponseSuccess(response)
            }
            override fun onFailure(call: Call<BaseResponse<String>>, t: Throwable) {
                setSettingUserView.onSetSettingUserViewFailure(4000, "데이터베이스 연결에 실패하였습니다.")
            }
        })
    }


    fun baseResponseSuccess(response : Response<BaseResponse<String>>){
        val baseResponse: BaseResponse<String> = response.body()!!
        Log.e("setting/API",baseResponse.toString())
        when(baseResponse.code){
            1000 -> setSettingUserView.onSetSettingUserViewSuccess(baseResponse.result)
            else -> setSettingUserView.onSetSettingUserViewFailure(baseResponse.code, baseResponse.message)
        }
    }

}