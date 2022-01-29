package com.likefirst.btos.data.entities

import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
data class DiaryInfo(
    val diaryDate: String?,
    val doneLists: ArrayList<String> = arrayListOf(),
    val emotionIdx: Int?,
    val contents: String,
    val userName: String
) : Parcelable {

}
