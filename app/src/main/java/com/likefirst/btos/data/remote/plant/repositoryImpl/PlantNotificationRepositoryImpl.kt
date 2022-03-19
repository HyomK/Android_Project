package com.likefirst.btos.data.remote.plant.repositoryImpl

import android.app.Application
import android.content.Context
import com.likefirst.btos.data.local.PlantInfoDatabase
import com.likefirst.btos.data.module.Plant
import com.likefirst.btos.data.remote.plant.dataSource.PlantInfoRemoteDataSource
import com.likefirst.btos.domain.repository.PlantInfoRepository
import com.likefirst.btos.domain.repository.PlantNotificationRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ServiceScoped
import javax.inject.Inject

class PlantNotificationRepositoryImpl @Inject constructor( private val repository : PlantInfoRepository): PlantNotificationRepository{

    override fun getSelectedPlant(): Plant {
        return repository.getSelectedPlant()
    }

    override  fun setInitPlant(idx: Int, status:String, level : Int, isOwn : Boolean){
        repository.setInitPlant(idx, status, level, isOwn)
    }
}