package com.likefirst.btos.data.remote.plant.repositoryImpl

import androidx.lifecycle.LiveData
import com.likefirst.btos.data.entities.Plant
import com.likefirst.btos.data.local.PlantInfoDao
import com.likefirst.btos.data.module.PlantRequest
import com.likefirst.btos.data.module.PlantResponse
import com.likefirst.btos.data.remote.plant.dataSource.PlantInfoRemoteDataSource
import com.likefirst.btos.domain.repository.PlantInfoRepository
import retrofit2.Response
import javax.inject.Inject

class PlantInfoRepositoryImpl @Inject constructor(private val remoteDataSource : PlantInfoRemoteDataSource, private val mPlantDao: PlantInfoDao) :
    PlantInfoRepository {
    override suspend fun loadPlantList(id:Int): Response<PlantResponse> {
        return remoteDataSource.loadPlantList(id)
    }

    override suspend fun selectPlant(request: PlantRequest): Response<PlantResponse> {
       return remoteDataSource.selectPlant(request)
    }

    override suspend fun buyPlant(request: PlantRequest): Response<PlantResponse> {
        return remoteDataSource.buyPlant(request)
    }


    override  fun getPlantList(): LiveData<List<Plant>> {
        return mPlantDao.getPlants()
    }

    override suspend fun insert(plant : Plant) {
        mPlantDao.insert(plant)
    }

    override  fun getPlant(idx: Int) : Plant {
        return mPlantDao.getPlant(idx)
    }

    override fun getCurrentPlant():LiveData<Plant>{
        return mPlantDao.getCurrentPlant()
    }

    override  fun getSelectedPlant(): Plant {
        return mPlantDao.getSelectedPlant("selected")!!
    }

    override  fun setInitPlant(idx: Int, status:String, level : Int, isOwn : Boolean){
        mPlantDao.setPlantInit(idx, status, level, isOwn)
    }

    override fun setPlantStatus(idx: Int, status:String){
        mPlantDao.setPlantStatus(idx,status)
    }
}