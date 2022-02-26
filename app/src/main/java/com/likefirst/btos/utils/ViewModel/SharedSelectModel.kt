package com.likefirst.btos.utils.ViewModel

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.likefirst.btos.utils.errorDialog

class SharedSelectModel:ViewModel(){
    private val liveData =  MutableLiveData<Bundle>()
    private var isSuccess =MutableLiveData<Boolean>()
    fun  getLiveData() :LiveData<Bundle>{ return liveData }
    fun setLiveData(data:Bundle)  {
        liveData.setValue(data)
    }
    fun removeLiveData(){
        liveData.value= bundleOf("plantName" to null , "plantIdx" to 1, "level" to 0)
    }

    fun isSuccess():LiveData<Boolean> {return isSuccess }
    fun setResult(result : Boolean){isSuccess.setValue(result)}
}