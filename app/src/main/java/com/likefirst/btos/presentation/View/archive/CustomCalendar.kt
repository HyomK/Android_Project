package com.likefirst.btos.presentation.View.archive

import com.likefirst.btos.data.remote.viewer.response.ArchiveCalendar
import com.likefirst.btos.utils.stringToDate
import java.util.*
import kotlin.collections.ArrayList

class CustomCalendar(date : Date, val inputDataList : ArrayList<ArchiveCalendar>) {

    companion object {
        const val DAYS_OF_WEEK = 7
        const val LOW_OF_CALENDAR = 6
    }

    val calendar = Calendar.getInstance()

    var prevTail = 0
    var nextHead = 0
    var currentMaxDate = 0

    var dateList = arrayListOf<ArchiveCalendar>()

    init {
        calendar.time = date
    }

    fun initBaseCalendar() {
        makeMonthDate()
    }

    private fun makeMonthDate() {

        dateList.clear()

        calendar.set(Calendar.DATE, 1)

        //현재 달의 최대 날짜 수
        currentMaxDate = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)

        //달력 시작 전 날짜의 빈칸 개수
        prevTail = calendar.get(Calendar.DAY_OF_WEEK) - 1

        makePrevTail(calendar.clone() as Calendar)
        makeCurrentMonth(calendar)

        nextHead = LOW_OF_CALENDAR * DAYS_OF_WEEK - (prevTail + currentMaxDate)
        makeNextHead()

        insertDiaryInfo()
    }

    private fun makePrevTail(calendar: Calendar) {
        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - 1)
        val maxDate = calendar.getActualMaximum(Calendar.DATE)
        var maxOffsetDate = maxDate - prevTail

//        for (i in 1..prevTail) dateList.add(++maxOffsetDate)
        for (i in 1..prevTail) dateList.add(ArchiveCalendar(null, null, 0, null, null))
    }

    private fun makeCurrentMonth(calendar: Calendar) {
        for (i in 1..calendar.getActualMaximum(Calendar.DATE)) dateList.add(ArchiveCalendar(null, null, i, null, null))
    }

    private fun makeNextHead() {
        var date = 1

//        for (i in 1..nextHead) dateList.add(date++)
        for (i in 1..nextHead) dateList.add(ArchiveCalendar(null, null, 0, null, null))    }

    private fun insertDiaryInfo(){
        for (inputData in inputDataList){
            val dateString = inputData.diaryDate!!
            val date = stringToDate(dateString)
            calendar.time = date
            val position = dateList.indexOf(ArchiveCalendar(null, null, calendar.get(Calendar.DAY_OF_MONTH), null, null))
            dateList[position] = ArchiveCalendar(inputData.diaryDate, inputData.diaryIdx, calendar.get(Calendar.DAY_OF_MONTH), inputData.doneListNum, inputData.emotionIdx)
        }
    }
}