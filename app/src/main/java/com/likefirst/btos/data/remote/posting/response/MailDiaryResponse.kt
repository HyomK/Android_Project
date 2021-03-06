package com.likefirst.btos.data.remote.posting.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@Parcelize
data class  DoneList(
    @SerializedName("doneIdx") val doneIdx : Int,
    @SerializedName("content") val content : String,
):Parcelable

@Parcelize
data class Diary(
    @SerializedName("diaryIdx") val diaryIdx : Int,
    @SerializedName("emotionIdx") val emotionIdx : Int,
    @SerializedName("diaryDate") val diaryDate : String,
    @SerializedName("content") val content :String,
    @SerializedName("doneList") val doneList :ArrayList<DoneList>,

):Parcelable


data class MailDiaryResponse(
    @SerializedName("diaryIdx") val diaryIdx : Int,
    @SerializedName("emotionIdx") val emotionIdx : Int,
    @SerializedName("diaryDate") val diaryDate : String,
    @SerializedName("content") val content :String,
    @SerializedName("doneList") val doneList :ArrayList<DoneList>,
    @SerializedName("senderNickName") val senderNickName :String,
    @SerializedName("senderFontIdx") val senderFontIdx :Int

)

data class PostDiaryResponse(
    @SerializedName("diaryIdx") val diaryIdx : Int
)