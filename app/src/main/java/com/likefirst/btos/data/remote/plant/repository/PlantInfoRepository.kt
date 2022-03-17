package com.likefirst.btos.data.remote.plant.repository

import androidx.lifecycle.LiveData
import com.likefirst.btos.data.module.Plant
import com.likefirst.btos.data.module.PlantRequest
import com.likefirst.btos.data.module.PlantResponse
import com.likefirst.btos.data.remote.plant.view.PlantBuyView
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface PlantInfoRepository {
    fun loadPlantList(id:Int) : Single<PlantResponse>
    fun selectPlant(request: PlantRequest): Single<PlantResponse>
    fun buyPlant(request: PlantRequest):Single<PlantResponse>
    fun getPlantList(): LiveData<List<Plant>>
    suspend fun insert(plant : Plant)
    fun getPlant(idx: Int) : Plant
    fun getCurrentPlant():LiveData<Plant>
    fun getSelectedPlant():Plant
    fun setInitPlant(idx: Int, status:String, level : Int, isOwn : Boolean)
    fun setPlantStatus(idx: Int, status:String)
}