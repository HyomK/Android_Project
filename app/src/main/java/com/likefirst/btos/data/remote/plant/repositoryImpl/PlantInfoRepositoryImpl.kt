package com.likefirst.btos.data.remote.plant.repositoryImpl

import androidx.lifecycle.LiveData
import androidx.room.Database
import com.likefirst.btos.data.module.Plant
import com.likefirst.btos.data.local.PlantInfoDao
import com.likefirst.btos.data.module.ApiInterface
import com.likefirst.btos.data.module.PlantRequest
import com.likefirst.btos.data.module.PlantResponse
import com.likefirst.btos.data.remote.plant.repository.PlantInfoRepository
import io.reactivex.Single
import javax.inject.Inject

class PlantInfoRepositoryImpl @Inject constructor(private val apiInterface: ApiInterface, private val mPlantDao: PlantInfoDao) : PlantInfoRepository {
    override fun loadPlantList(id:Int): Single<PlantResponse> {
        return apiInterface.loadPlantList(id.toString())
    }

    override fun selectPlant(request: PlantRequest): Single<PlantResponse> {
        return apiInterface.selectPlant(request)
    }

    override fun buyPlant(request: PlantRequest): Single<PlantResponse> {
        return apiInterface.buyPlant(request)
    }


    fun getPlantList(): LiveData<List<Plant>> {
        return mPlantDao.getPlants()
    }

    fun insert(plant : Plant) {
        mPlantDao.insert(plant)
    }

    fun getPlant(idx: Int) : Plant{
        return mPlantDao.getPlant(idx)
    }

    fun getCurrentPlant():LiveData<Plant>{
        return mPlantDao.getCurrentPlant()
    }

    fun getSelectedPlant():Plant{
        return mPlantDao.getSelectedPlant("selected")!!
    }

    fun setInitPlant(idx: Int, status:String, level : Int, isOwn : Boolean){
        mPlantDao.setPlantInit(idx, status, level, isOwn)
    }

    fun setPlantStatus(idx: Int, status:String){
        mPlantDao.setPlantStatus(idx,status)
    }
}