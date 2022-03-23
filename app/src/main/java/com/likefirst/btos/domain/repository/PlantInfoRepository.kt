package com.likefirst.btos.domain.repository

import androidx.lifecycle.LiveData
import com.likefirst.btos.data.entities.Plant
import com.likefirst.btos.data.remote.plant.PlantRequest
import com.likefirst.btos.data.remote.plant.PlantResponse
import retrofit2.Response

interface PlantInfoRepository {
    suspend  fun loadPlantList(id:Int) : Response<PlantResponse>
    suspend fun selectPlant(request: PlantRequest): Response<PlantResponse>
    suspend fun buyPlant(request: PlantRequest):Response<PlantResponse>
    fun getPlantList(): LiveData<List<Plant>>
    suspend fun insert(plant : Plant)
    fun getPlant(idx: Int) : Plant
    fun getCurrentPlant():LiveData<Plant>
    fun getSelectedPlant(): Plant
    fun setInitPlant(idx: Int, status:String, level : Int, isOwn : Boolean)
    fun setPlantStatus(idx: Int, status:String)
}