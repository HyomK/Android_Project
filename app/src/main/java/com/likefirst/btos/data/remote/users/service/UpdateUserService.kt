package com.likefirst.btos.data.remote.users.service

import android.util.Log
import com.likefirst.btos.ApplicationClass.Companion.retrofit
import com.likefirst.btos.data.entities.UserIsSad
import com.likefirst.btos.data.remote.BaseResponse
import com.likefirst.btos.data.remote.users.view.UpdateIsSadView
import com.likefirst.btos.utils.RetrofitInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UpdateUserService {
    private lateinit var updateIsSadView: UpdateIsSadView
    // Retrofit 객체 생성
    val updateUserService = retrofit.create(RetrofitInterface::class.java)

    fun setUpdateIsSadView(updateIsSadView: UpdateIsSadView){
        this.updateIsSadView = updateIsSadView
    }

    fun updateIsSad(userIdx: Int, isSad: UserIsSad){
        updateIsSadView.onUpdateLoading()
        Log.d("isSad", isSad.toString())
        // API 호출
        updateUserService.updateIsSad(userIdx, isSad).enqueue(object : Callback<BaseResponse> {
            override fun onResponse(call: Call<BaseResponse>, response: Response<BaseResponse>) {
                Log.d("Response", response.toString())

                val resp = response.body()!!
                when (resp.code){
                    1000 -> {
                        updateIsSadView.onUpdateSuccess(isSad)
                        Log.d("APURESULT", resp.result)
                    }
                    else -> updateIsSadView.onUpdateFailure(resp.code, resp.message)
                }
            }
            override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                Log.d("UpdateIsSad/FAILURE", t.toString())

                updateIsSadView.onUpdateFailure(400, "네트워크 오류가 발생했습니다.")
            }
        })
    }
}