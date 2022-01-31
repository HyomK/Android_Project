package com.likefirst.btos.data.remote.service


import android.util.Log
import com.likefirst.btos.ApplicationClass
import com.likefirst.btos.data.remote.response.Plant
import com.likefirst.btos.data.remote.response.PlantResponse
import com.likefirst.btos.data.remote.response.ReplyResponse
import com.likefirst.btos.data.remote.view.PlantListView
import com.likefirst.btos.utils.RetrofitInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PlantService {

    private lateinit var plantView : PlantListView
    private val PlantService = ApplicationClass.retrofit.create(RetrofitInterface::class.java)

    fun setPlantListView(plantView: PlantListView){
        this.plantView=plantView
    }

    fun loadPlantList(userId:String){
        plantView.onPlantListLoading()

        PlantService.loadPlantList(userId).enqueue(object:Callback<PlantResponse>{
            override fun onResponse(call: Call<PlantResponse>, response: Response<PlantResponse>) {
                val plantResponse: PlantResponse =response.body()!!
                Log.e("Reply/API", plantResponse.toString())

                when(plantResponse.code){
                    1000->plantView.onPlantListSuccess( plantResponse.result)
                    else->plantView.onPlantListFailure( plantResponse.code,plantResponse.message)
                }
            }

            override fun onFailure(call: Call<PlantResponse>, t: Throwable) {
                plantView.onPlantListFailure( 4000,"데이터베이스 연결에 실패하였습니다.")
            }

        })
    }

    fun selectPlant(userId:String, plantId:String){
        PlantService.selectPlant(userId,plantId).enqueue(object:Callback<PlantResponse>{
            override fun onResponse(call: Call<PlantResponse>, response: Response<PlantResponse>) {
                val plantResponse: PlantResponse =response.body()!!
                Log.e("Plant/API", plantResponse.toString())
                when(plantResponse.code){
                    1000->plantView.onPlantSelectSuccess( plantResponse.isSuccess)
                    else->plantView.onPlantListFailure( plantResponse.code,plantResponse.message)
                }
            }

            override fun onFailure(call: Call<PlantResponse>, t: Throwable) {
                plantView.onPlantSelectFailure( 4000,"데이터베이스 연결에 실패하였습니다.")
            }
        })

    }
    fun buyPlant(userId:String, plantId:String){
        PlantService.buyPlant(userId,plantId).enqueue(object:Callback<PlantResponse>{
            override fun onResponse(call: Call<PlantResponse>, response: Response<PlantResponse>) {
                val plantResponse: PlantResponse =response.body()!!
                Log.e("Plant/API", plantResponse.toString())
                when(plantResponse.code){
                    1000->plantView.onPlantBuySuccess( plantResponse.isSuccess)
                    else->plantView.onPlantBuyFailure( plantResponse.code,plantResponse.message)
                }
            }

            override fun onFailure(call: Call<PlantResponse>, t: Throwable) {
                plantView.onPlantBuyFailure( 4000,"데이터베이스 연결에 실패하였습니다.")
            }

        })
    }

}