package com.likefirst.btos.data.remote.plant.view

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.likefirst.btos.data.entities.Plant

class SharedBuyModel: ViewModel(){
    private val liveData =  MutableLiveData<Bundle>();
    private var isSuccess = MutableLiveData<Boolean>()
    fun  getLiveData() : LiveData<Bundle> { return liveData }
    fun setLiveData(data: Bundle)  {
        Log.e("Plant/ buy model ",isSuccess.toString())
        if(isSuccess().value==true){
            liveData.setValue(data)
        } }
    fun isSuccess():LiveData<Boolean> {return isSuccess }
    fun setResult(result : Boolean){isSuccess.setValue(result)}
}
