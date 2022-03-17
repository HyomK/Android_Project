package com.likefirst.btos.data.remote.plant.dataSource

import com.likefirst.btos.data.module.ApiInterface
import com.likefirst.btos.data.module.PlantRequest
import com.likefirst.btos.data.module.PlantResponse
import io.reactivex.Single
import javax.inject.Inject

class PlantInfoDataSource @Inject constructor( val plantApi : ApiInterface) {

    fun loadPlantList(id: Int): Single<PlantResponse>{
        return plantApi.loadPlantList(id.toString())
    }

    fun selectPlant(request : PlantRequest) :Single<PlantResponse>{
        return plantApi.selectPlant(request)
    }

    fun buyPlant(request : PlantRequest):Single<PlantResponse>{
        return plantApi.buyPlant(request)
    }

}