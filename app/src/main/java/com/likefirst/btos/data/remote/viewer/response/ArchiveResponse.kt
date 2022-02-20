package com.likefirst.btos.data.remote.viewer.response

import com.google.gson.annotations.SerializedName

data class ArchiveCalendar(
    @SerializedName("diaryDate") val diaryDate : String?,
    @SerializedName("diaryIdx") val diaryIdx : Int?,
    val dateInt : Int = 0,
    @SerializedName("doneListNum") val doneListNum : Int?,
    @SerializedName("emotionIdx") val emotionIdx : Int?,
)

data class ArchiveList(
    @SerializedName("isSuccess") val isSuccess : Boolean,
    @SerializedName("code") val code : Int,
    @SerializedName("message") val message : String,
    @SerializedName("result") val result : ArrayList<ArchiveListResult>,
    @SerializedName("pageInfo") val pageInfo : ArchiveListPageInfo,
    )

data class ArchiveListResult(
    @SerializedName("month") val month : String,
    @SerializedName("diaryList") val diaryList : ArrayList<ArchiveListDiaryList>,
    )

data class ArchiveListDiaryList(
    @SerializedName("diaryIdx") val diaryIdx : Int,
    @SerializedName("doneListNum") var doneListNum : Int,
    @SerializedName("emotionIdx") var emotionIdx : Int,
    @SerializedName("diaryDate") val diaryDate : String,
    @SerializedName("content") var content : String,
    )

data class ArchiveListPageInfo(
    @SerializedName("hasNext") val hasNext : Boolean,
    @SerializedName("currentPage") val currentPage : Int,
    @SerializedName("startPage") val startPage : Int,
    @SerializedName("endPage") val endPage : Int,
    @SerializedName("dataNumPerPage") val dataNumPerPage : Int,
    @SerializedName("dataNum_currentPage") val dataNum_currentPage : Int,
    @SerializedName("dataNum_total") val dataNum_total : Int
    )

// 일기 조회
data class ArchiveDiaryResult(
    @SerializedName("diaryIdx") val diaryIdx : Int,
    @SerializedName("emotionIdx") val emotionIdx : Int,
    @SerializedName("diaryDate") val diaryDate : String,
    @SerializedName("isPublic") val isPublic : Int,
    @SerializedName("content") val content : String,
    @SerializedName("doneList") val doneList : ArrayList<String>
    )