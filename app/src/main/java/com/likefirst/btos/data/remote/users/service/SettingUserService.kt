package com.likefirst.btos.data.remote.users.service

import android.util.Log
import com.likefirst.btos.ApplicationClass.Companion.retrofit
import com.likefirst.btos.data.entities.UserBirth
import com.likefirst.btos.data.entities.UserName
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

        SettingUserService.setName(userIdx, nickname).enqueue(object: Callback<BaseResponse> {

            override fun onResponse(call: Call<BaseResponse>, response: Response<BaseResponse>) {

                val baseResponse: BaseResponse = response.body()!!

                Log.e("setName/API",baseResponse.toString())
                when(baseResponse.code){
                    1000 -> setSettingUserView.onSetSettingUserViewSuccess(baseResponse.result)
                    else -> setSettingUserView.onSetSettingUserViewFailure(baseResponse.code, baseResponse.message)
                }
            }
            override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                setSettingUserView.onSetSettingUserViewFailure(4000, "데이터베이스 연결에 실패하였습니다.")
            }
        })
    }

    fun setBirth(userIdx : Int, birth : UserBirth){

        setSettingUserView.onSetSettingUserViewLoading()

        SettingUserService.setBirth(userIdx, birth).enqueue(object: Callback<BaseResponse> {

            override fun onResponse(call: Call<BaseResponse>, response: Response<BaseResponse>) {

                val baseResponse: BaseResponse = response.body()!!

                Log.e("setBirth/API",baseResponse.toString())

                when(baseResponse.code){
                    1000 -> setSettingUserView.onSetSettingUserViewSuccess(baseResponse.result)
                    else -> setSettingUserView.onSetSettingUserViewFailure(baseResponse.code, baseResponse.message)
                }
            }

            override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                setSettingUserView.onSetSettingUserViewFailure(4000, "데이터베이스 연결에 실패하였습니다.")
            }

        })
    }

}