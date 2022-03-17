package com.likefirst.btos.data.remote.plant.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.likefirst.btos.data.module.Plant
import com.likefirst.btos.data.module.PlantRequest
import com.likefirst.btos.data.remote.plant.repository.PlantInfoRepository
import com.likefirst.btos.data.remote.plant.view.PlantBuyView
import com.likefirst.btos.data.remote.plant.view.PlantSelectView
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject


@HiltViewModel
class PlantInfoViewModel @Inject constructor(private val repository : PlantInfoRepository) : ViewModel(){


    private val plantEmitter = MutableLiveData<List<Plant>>()
    val plants : LiveData<List<Plant>> = plantEmitter
    private val allPlant : LiveData<List<Plant>>
    private val currentPlant : LiveData<Plant>

    init {
        allPlant  = repository.getPlantList()
        currentPlant =repository.getCurrentPlant()
        plantEmitter.value = repository.getPlantList().value
    }

    fun insert(plant : Plant) {
        CoroutineScope(Dispatchers.IO).launch{
            repository.insert(plant)
        }
    }

    fun getPlantList(): LiveData<List<Plant>> {
        return repository.getPlantList()
    }

    fun getCurrentPlant(): LiveData<Plant> {
        return repository.getCurrentPlant()
    }

    fun getSelectedPlant(): Plant {

        return  repository.getSelectedPlant()
    }

    fun setPlantStatus(idx:Int, status :String){
        return repository.setPlantStatus(idx,status)
    }

    fun selectPlant(userIdx : Int, plantIdx: Int)= CoroutineScope(Dispatchers.IO).async{
        val request =PlantRequest(userIdx,plantIdx)
        repository.selectPlant(request)
    }

    fun buyPlant( userIdx : Int, plantIdx: Int)= CoroutineScope(Dispatchers.IO).async {
        val request =PlantRequest(userIdx,plantIdx)
        repository.buyPlant(request)
    }

    fun getPlant(idx: Int) : Plant? {
       var result : Plant?=null
        runBlocking{
            val cs = CoroutineScope(Dispatchers.IO)
            val job = cs.launch {
                result = repository.getPlant(idx)
            }
            job.join()
        }
        return result
    }

    fun setInitPlant(idx: Int, status:String, level : Int, isOwn : Boolean)=
        CoroutineScope(Dispatchers.IO).launch {
        repository.setInitPlant(idx,status,level,isOwn )
    }
}