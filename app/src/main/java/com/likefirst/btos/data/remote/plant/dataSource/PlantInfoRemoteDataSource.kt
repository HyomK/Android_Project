package com.likefirst.btos.data.remote.plant.dataSource

import com.likefirst.btos.data.remote.plant.PlantApiInterface
import com.likefirst.btos.data.remote.plant.PlantRequest
import com.likefirst.btos.data.remote.plant.PlantResponse
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