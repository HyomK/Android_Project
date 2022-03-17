package com.likefirst.btos.data.remote.viewer.view

import com.likefirst.btos.data.remote.viewer.response.*
import com.likefirst.btos.presentation.view.archive.ArchiveListRVAdapter

interface ArchiveCalendarView {
    fun onArchiveCalendarLoading()
    fun onArchiveCalendarSuccess(result: ArrayList<ArchiveCalendar>)
    fun onArchiveCalendarFailure(code : Int)
}

interface ArchiveListView{
    fun onArchiveListLoading()
    fun onArchiveListSuccess(result: ArrayList<ArchiveListResult>, pageInfo: ArchiveListPageInfo, adapter : ArchiveListRVAdapter)
    fun onArchiveListFailure(code: Int)
}

interface ArchiveDiaryView{
    fun onArchiveDiaryLoading()
    fun onArchiveDiarySuccess(result : ArchiveDiaryResult)
    fun onArchiveDiaryFailure(code : Int)
}