package com.likefirst.btos.data.remote.plant.dataSource

import com.likefirst.btos.data.module.PlantApiInterface
import com.likefirst.btos.data.module.PlantRequest
import com.likefirst.btos.data.module.PlantResponse
import retrofit2.Response
import javax.inject.Inject

class PlantInfoRemoteDataSource @Inject constructor(val plantApi : PlantApiInterface) {

    suspend fun loadPlantList(id: Int): Response<PlantResponse>{
        return  plantApi.loadPlantList(id.toString())
    }

    suspend  fun selectPlant(request : PlantRequest) : Response<PlantResponse>{
        return  plantApi.selectPlant(request)
    }

    suspend  fun buyPlant(request : PlantRequest): Response<PlantResponse>{
        return  plantApi.buyPlant(request)
    }

}