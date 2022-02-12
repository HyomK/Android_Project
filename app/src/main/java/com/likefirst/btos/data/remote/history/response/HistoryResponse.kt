package com.likefirst.btos.data.remote.history.response

import com.google.gson.annotations.SerializedName
import com.likefirst.btos.data.entities.Content
import com.likefirst.btos.data.entities.HistoryList
import com.likefirst.btos.data.entities.PageInfo

data class HistoryBaseResponse<T>(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code : Int,
    @SerializedName("message") val message : String,
    @SerializedName("result") val result : T,
    @SerializedName("pageInfo") val pageInfo : PageInfo,
)

data class HistorySenderDetailResponse(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code : Int,
    @SerializedName("message") val message : String,
    @SerializedName("result") val result : ArrayList<Content>,
    @SerializedName("pageInfo") val pageInfo : PageInfo,
)

data class HistoryDetailResponse(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code : Int,
    @SerializedName("message") val message : String,
    @SerializedName("result") val result : ArrayList<HistoryList>,
)