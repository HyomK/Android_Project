package com.likefirst.btos.data.remote.viewer.view

import com.likefirst.btos.data.remote.viewer.response.ArchiveCalendar

interface ArchiveCalendarView {
    fun onArchiveCalendarLoading()
    fun onArchiveCalendarSuccess(result: ArrayList<ArchiveCalendar>)
    fun onArchiveCalendarFailure(code : Int)
}