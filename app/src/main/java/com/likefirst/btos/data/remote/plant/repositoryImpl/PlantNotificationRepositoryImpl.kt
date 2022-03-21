package com.likefirst.btos.data.remote.plant.repositoryImpl
import com.likefirst.btos.data.entities.Plant
import com.likefirst.btos.domain.repository.PlantInfoRepository
import com.likefirst.btos.domain.repository.PlantNotificationRepository
import javax.inject.Inject

class PlantNotificationRepositoryImpl @Inject constructor( private val repository : PlantInfoRepository): PlantNotificationRepository{
    override fun getSelectedPlant(): Plant {
        return repository.getSelectedPlant()
    }
    override  fun setInitPlant(idx: Int, status:String, level : Int, isOwn : Boolean){
        repository.setInitPlant(idx, status, level, isOwn)
    }
}