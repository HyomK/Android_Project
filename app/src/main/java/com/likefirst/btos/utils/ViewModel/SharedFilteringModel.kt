package com.likefirst.btos.utils.ViewModel

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedFilteringModel : ViewModel(){
    private val liveFilteringData =  MutableLiveData<String>()
    fun  getLiveFilteringData() : LiveData<String> { return liveFilteringData }
    fun setLivFilteringData(data: String)  { liveFilteringData.setValue(data) }
}