package com.likefirst.btos.data.remote.plant.dataSource

import com.likefirst.btos.data.module.PlantApiInterface
import com.likefirst.btos.data.module.PlantRequest
import com.likefirst.btos.data.module.PlantResponse
import retrofit2.Response
import javax.inject.Inject

class PlantInfoRemoteDataSource @Inject constructor(val plantPlantApi : PlantApiInterface) {

    suspend fun loadPlantList(id: Int): Response<PlantResponse>{
        return plantPlantApi.loadPlantList(id.toString())
    }

    suspend  fun selectPlant(request : PlantRequest) : Response<PlantResponse>{
        return plantPlantApi.selectPlant(request)
    }

    suspend  fun buyPlant(request : PlantRequest): Response<PlantResponse>{
        return plantPlantApi.buyPlant(request)
    }

}