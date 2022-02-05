package com.likefirst.btos.data.remote.viewer.response

import com.google.gson.annotations.SerializedName

data class ArchiveCalendar(
    @SerializedName("diaryDate") val diaryDate : String?,
    val dateInt : Int = 0,
    @SerializedName("doneListNum") val doneListNum : Int?,
    @SerializedName("emotionIdx") val emotionIdx : Int?,
)