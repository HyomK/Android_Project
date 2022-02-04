package com.likefirst.btos.data.entities

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
data class DiaryViewerInfo(
    @ColumnInfo(name = "senderNickName") val userName : String = "(알 수 없음)",
    @ColumnInfo(name = "emotionIdx") val emotionIdx : Int? = null,
    @ColumnInfo(name = "diaryDate") val diaryDate : String = "1900.01.01",
    @ColumnInfo(name = "diaryContent") val contents : String = "",
    @ColumnInfo(name = "isPublic") val isPublic : Boolean = false,
    @ColumnInfo(name = "doneList") val doneLists : ArrayList<String> = arrayListOf()
) : Parcelable {

}

@Parcelize
data class PostDiary(
    @SerializedName("userIdx") val userIdx : Int = 0,
    @SerializedName("emotionIdx") val emotionIdx : Int? = null,
    @SerializedName("diaryDate") val diaryDate : String = "1900.01.01",
    @SerializedName("diaryContent") val contents : String = "",
    @SerializedName("isPublic") val isPublic : Boolean = false,
    @SerializedName("doneList") val doneLists : ArrayList<String> = arrayListOf()
) : Parcelable{

}