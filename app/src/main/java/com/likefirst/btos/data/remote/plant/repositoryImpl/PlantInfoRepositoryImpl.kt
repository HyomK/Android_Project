package com.likefirst.btos.data.remote.plant.repositoryImpl

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.room.Database
import com.likefirst.btos.data.module.Plant
import com.likefirst.btos.data.local.PlantInfoDao
import com.likefirst.btos.data.module.ApiInterface
import com.likefirst.btos.data.module.PlantRequest
import com.likefirst.btos.data.module.PlantResponse
import com.likefirst.btos.data.remote.plant.dataSource.PlantInfoDataSource
import com.likefirst.btos.data.remote.plant.repository.PlantInfoRepository
import io.reactivex.Observable.fromIterable
import io.reactivex.Single
import java.util.*
import javax.inject.Inject

class PlantInfoRepositoryImpl @Inject constructor(private val dataSource : PlantInfoDataSource, private val mPlantDao: PlantInfoDao) : PlantInfoRepository {
    override fun loadPlantList(id:Int): Single<PlantResponse> {
        return dataSource.loadPlantList(id)
    }

    override fun selectPlant(request: PlantRequest): Single<PlantResponse> {
       return dataSource.selectPlant(request)
    }

    override fun buyPlant(request: PlantRequest): Single<PlantResponse> {
        return dataSource.buyPlant(request)
    }


    override  fun getPlantList(): LiveData<List<Plant>> {
        return mPlantDao.getPlants()
    }

    override suspend fun insert(plant : Plant) {
        Log.e("HILT_TEST",plant.toString())
        mPlantDao.insert(plant)
    }

    override  fun getPlant(idx: Int) : Plant{
        return mPlantDao.getPlant(idx)
    }

    override fun getCurrentPlant():LiveData<Plant>{
        return mPlantDao.getCurrentPlant()
    }

    override  fun getSelectedPlant():Plant{
        return mPlantDao.getSelectedPlant("selected")!!
    }

    override  fun setInitPlant(idx: Int, status:String, level : Int, isOwn : Boolean){
        mPlantDao.setPlantInit(idx, status, level, isOwn)
    }

    override fun setPlantStatus(idx: Int, status:String){
        mPlantDao.setPlantStatus(idx,status)
    }
}