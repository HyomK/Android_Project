package com.likefirst.btos.ui.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.likefirst.btos.data.entities.Plant
import com.likefirst.btos.data.module.PlantRequest
import com.likefirst.btos.data.module.PlantResponse
import com.likefirst.btos.domain.repository.PlantInfoRepository
import com.likefirst.btos.domain.ApiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject


@HiltViewModel
class PlantInfoViewModel @Inject constructor(private val repository : PlantInfoRepository) : ViewModel(){


    private val plantEmitter = MutableLiveData<List<Plant>>()
    val plants : LiveData<List<Plant>> = plantEmitter

    private val  currentPlantEmitter = MutableLiveData<Plant>()
    val currentPlant : LiveData<Plant> = currentPlantEmitter


    private val _res = MutableLiveData<ApiResult<PlantResponse>>()
    val res : LiveData<ApiResult<PlantResponse>>
        get() = _res



    init {
        currentPlantEmitter.value = repository.getCurrentPlant().value
        plantEmitter.value = repository.getPlantList().value
    }



    fun selectPlantItem(request : PlantRequest)  = CoroutineScope(Dispatchers.IO).launch {
        _res.postValue(ApiResult.loading())
         repository.selectPlant(request).let {
            Log.e("plant-select",request.toString())
            if (it.isSuccessful){
                _res.postValue(ApiResult.success(it.code(),it.body()))
                val selected = repository.getSelectedPlant()
                Log.e("plant-select",selected.toString())
                repository.setPlantStatus(selected.plantIdx,"active")
                repository.setPlantStatus(request.plantIdx,"selected")
            }else{
                _res.postValue(ApiResult.error(it.code(),it.message()))
            }
        }
    }


    fun buyPlantItem(request : PlantRequest)  = CoroutineScope(Dispatchers.IO).launch {
        _res.postValue(ApiResult.loading())
        repository.buyPlant(request).let {
            if (it.isSuccessful){
                _res.postValue(ApiResult.success(it.code(),it.body()))
                val plant = repository.getPlant(request.plantIdx)
                if(plant!=null){
                   repository.setInitPlant(request.plantIdx,"active",0,true)
                }
            }else{
                _res.postValue(ApiResult.error(it.code(),it.message()))
            }
        }
    }

    fun loadPlantItemList(userIdx: Int) = CoroutineScope(Dispatchers.IO).launch {
        _res.postValue(ApiResult.loading())
        repository.loadPlantList(userIdx).let{
            if (it.isSuccessful){
                _res.postValue(ApiResult.success(it.code(),it.body()))
                val plantList = it.body()!!.result!!
                plantList.forEach { i ->
                    run {
                        if (getPlant(i.plantIdx) == null) {
                            insert(i)
                        } else {
                            setInitPlant(i.plantIdx,i.plantStatus,i.currentLevel,i.isOwn)
                        }
                    }
                }
            }else{
                _res.postValue(ApiResult.error(it.code(),it.message()))
            }
        }
    }



    fun insert(plant : Plant) {
        CoroutineScope(Dispatchers.IO).launch{
            repository.insert(plant)
        }
    }

    fun getPlantList(): LiveData<List<Plant>> {
        return repository.getPlantList()
    }

    fun getCurrentPlantInfo(): LiveData<Plant> {
        return repository.getCurrentPlant()
    }

    fun getSelectedPlant(): Plant {
        var result : Plant? =null
        runBlocking{
            val cs = CoroutineScope(Dispatchers.IO)
            val job = cs.launch {
               // result = repository.getSelectedPlant()
                result =repository.getPlant(1)
            }
            job.join()
        }
        return result!!
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