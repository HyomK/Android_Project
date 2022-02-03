package com.likefirst.btos.ui.profile.plant

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel:ViewModel(){
    private val liveData =  MutableLiveData<Bundle>();
    fun  getLiveData() :LiveData<Bundle>{ return liveData }
    fun setLiveData(data:Bundle)  { liveData.setValue(data) }
}