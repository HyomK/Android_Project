package com.likefirst.btos.data.remote.viewer.response

import com.google.gson.annotations.SerializedName

data class UpdateDiaryRequest(
    @SerializedName("diaryIdx") val diaryIdx : Int,
    @SerializedName("userIdx") val userIdx : Int,
    @SerializedName("emotionIdx") val emotionIdx : Int,
    @SerializedName("diaryDate") val diaryDate : String,
    @SerializedName("diaryContent") val diaryContent : String,
    @SerializedName("isPublic") val isPublic : Int,
    @SerializedName("doneList") val doneList : ArrayList<String>
    )
