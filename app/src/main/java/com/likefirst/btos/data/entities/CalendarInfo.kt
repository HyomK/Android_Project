package com.likefirst.btos.data.entities

import androidx.room.ColumnInfo

data class CalendarInfo(
    @ColumnInfo(name = "diaryDate") val diaryDate : String?,
    val dateInt : Int = 0,
    @ColumnInfo(name = "doneListNum") val doneListNum : Int?,
    @ColumnInfo(name = "emotionIdx") val emotionIdx : Int?
) {
}