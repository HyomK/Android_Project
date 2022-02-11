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
    @SerializedName("isPublic") val isPublic: Boolean,
    @SerializedName("content") val content :String,
    @SerializedName("doneList") val doneList :ArrayList<DoneList>,
    @SerializedName("senderNickName") val senderNickName :String,
    @SerializedName("senderFontIdx") val senderFontIdx :Int
):Parcelable


data class MailDiaryResponse(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code : Int,
    @SerializedName("message") val message : String,
    @SerializedName("result") val result : ResultDiary
)

data class ResultDiary(
    @SerializedName("type") val type : String,
    @SerializedName("content") val content :  Diary

)

data class PostDiaryResponse(
    @SerializedName("type") val type : String,
    @SerializedName("status") val status : String,
    @SerializedName("levelChanged") val levelChanged : Boolean
)