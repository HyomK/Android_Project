package com.likefirst.btos.data.remote.notify.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


data class NoticeAPIResponse(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code : Int,
    @SerializedName("message") val message : String,
    @SerializedName("result") val result : ArrayList<NoticeDetailResponse>
)


@Parcelize
data class NoticeDetailResponse(
    @SerializedName("noticeIdx") val noticeIdx : Int,
    @SerializedName("title") val title : String,
    @SerializedName("content") val content : String,
    @SerializedName("createdAt") val createdAt: String,
): Parcelable