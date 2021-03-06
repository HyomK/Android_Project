package com.likefirst.btos.data.remote.notify.viewmodel

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class NotifyViewModel: ViewModel() {
    private var isNewMessage = MutableLiveData<Boolean>()
    private var isNewNotice = MutableLiveData<Boolean>()

    fun getMsgLiveData(): LiveData<Boolean>{ return isNewMessage }
    fun setMsgLiveData(data: Boolean) {
        isNewMessage.setValue(data)
    }
    fun getNoticeLiveData():LiveData<Boolean>{return isNewNotice }
    fun setNoticeLiveData(data:  Boolean) {
        isNewNotice.setValue(data)
    }
}

