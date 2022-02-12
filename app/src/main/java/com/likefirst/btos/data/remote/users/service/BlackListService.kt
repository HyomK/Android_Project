package com.likefirst.btos.data.remote.users.service

import android.util.Log
import com.likefirst.btos.ApplicationClass
import com.likefirst.btos.data.remote.BaseResponse
import com.likefirst.btos.data.remote.users.response.BlackList
import com.likefirst.btos.data.remote.users.response.BlockUser
import com.likefirst.btos.data.remote.users.view.BlackListView
import com.likefirst.btos.data.remote.users.view.SetBlockView
import com.likefirst.btos.data.remote.users.view.UnblockView
import com.likefirst.btos.utils.RetrofitInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class BlackListService {
    private lateinit var setBlacklistView: BlackListView
    private lateinit var setBlockView: SetBlockView
    private lateinit var unBlockView: UnblockView

    val blackListService = ApplicationClass.retrofit.create(RetrofitInterface::class.java)

    fun setBlackListView(setBlackListView: BlackListView){
        this.setBlacklistView=setBlackListView
    }

    fun setBlockView(view: SetBlockView){
        this.setBlockView =view
    }

    fun setUnBlockView(view: UnblockView){
        this.unBlockView =view
    }

    fun setBlock(request : BlackList){
        blackListService.setBlock(request).enqueue(object :Callback<BaseResponse<Int>>{
            override fun onResponse(
                call: Call<BaseResponse<Int>>, response: Response<BaseResponse<Int>>) {
                val result = response.body()!!
                setBlockView.onSetBlockViewSuccess(result.result)
            }

            override fun onFailure(call: Call<BaseResponse<Int>>, t: Throwable) {
                setBlockView.onSetBlockViewFailure(4000,"데이터베이스 연결에 실패했습니다")
            }
        })
    }

    fun getBlackList(userIdx: Int){
        blackListService.getBlackList(userIdx = userIdx).enqueue(object :Callback<BaseResponse<ArrayList<BlockUser>>>{
            override fun onResponse(
                call: Call<BaseResponse<ArrayList<BlockUser>>>,
                response: Response<BaseResponse<ArrayList<BlockUser>>>,
            ) {
                val resp = response.body()!!
                when (resp.code){
                    1000 -> {
                        setBlacklistView.onGetBlockListViewSuccess(resp.result)
                    }
                    else ->   setBlacklistView.onGetBlockListViewFailure(resp.code, resp.message)
                }
            }
            override fun onFailure(call: Call<BaseResponse<ArrayList<BlockUser>>>, t: Throwable) {
            }
        })
    }

    fun unBlock(blockId:Int){
        blackListService.unBlock(blockIdx = blockId).enqueue(object:Callback<BaseResponse<String>>{
            override fun onResponse(
                call: Call<BaseResponse<String>>,
                response: Response<BaseResponse<String>>,
            ) {
                val resp = response.body()!!
                when (resp.code){
                    1000 -> {
                        unBlockView.onUnBlockViewSuccess(resp.result)
                    }
                    else ->  unBlockView.onUnBlockViewFailure(resp.code, resp.message)
                }
            }

            override fun onFailure(call: Call<BaseResponse<String>>, t: Throwable) {

            }

        })
    }
}