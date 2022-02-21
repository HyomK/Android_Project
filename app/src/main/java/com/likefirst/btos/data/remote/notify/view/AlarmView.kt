package com.likefirst.btos.data.remote.notify.view

import com.likefirst.btos.data.remote.notify.response.Alarm
import com.likefirst.btos.data.remote.notify.response.AlarmInfo

interface AlarmListView{
    fun onGetAlarmListSuccess(result : ArrayList<Alarm>)
    fun onGetAlarmListFailure(code : Int, message : String)
}

interface AlarmInfoView{
    fun onGetAlarmInfoViewSuccess(result: AlarmInfo)
    fun onGetAlarmInfoFailure(code : Int, message : String)
}