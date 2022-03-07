package com.likefirst.btos.data.remote.plant.viewmodel

import android.app.Application
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.lifecycle.*
import com.likefirst.btos.ApplicationClass
import com.likefirst.btos.data.entities.Plant
import com.likefirst.btos.data.local.PlantDatabase
import com.likefirst.btos.data.local.PlantRepository
import com.likefirst.btos.data.remote.plant.response.PlantRequest
import com.likefirst.btos.data.remote.plant.service.PlantService
import com.likefirst.btos.data.remote.plant.view.PlantBuyView
import com.likefirst.btos.data.remote.plant.view.PlantSelectView
import kotlinx.coroutines.*

class PlantViewModel(application: Application): AndroidViewModel(application) {
    private val repository: PlantRepository
    private val allPlant : LiveData<List<Plant>>
    private val currentPlant : LiveData<Plant>
    private val plantService = PlantService()

    init {
        repository = PlantRepository(application)
        allPlant  = repository.getPlantList()
        currentPlant =repository.getCurrentPlant()
    }
    fun insert(plant : Plant) {
        repository.insert(plant)
    }

   fun getPlantList():LiveData<List<Plant>>{
        return allPlant
   }

   fun getCurrentPlant():LiveData<Plant>{
       return repository.getCurrentPlant()
   }

   fun getSelectedPlant():Plant{

      return  repository.getSelectedPlant()
   }

   fun setPlantStatus(idx:Int, status :String){
        return repository.setPlantStatus(idx,status)
   }

   fun selectPlant(view: PlantSelectView,userIdx : Int, plantIdx: Int)= CoroutineScope(Dispatchers.IO).async{
           plantService.setPlantSelectView(view)
           plantService.selectPlant(PlantRequest(userIdx,plantIdx))
   }

    fun buyPlant(view: PlantBuyView, userIdx : Int, plantIdx: Int)= CoroutineScope(Dispatchers.IO).async {
        plantService.setPlantBuyView(view)
        plantService.buyPlant(PlantRequest(userIdx,plantIdx))
    }

    fun getPlant(idx: Int) : Plant? {return repository.getPlant(idx)}

    fun setInitPlant(idx: Int, status:String, level : Int, isOwn : Boolean)=CoroutineScope(Dispatchers.IO).launch {
        repository.setInitPlant(idx,status,level,isOwn )
    }
}