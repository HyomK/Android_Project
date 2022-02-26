package com.likefirst.btos.data.remote.notify.response

import com.google.gson.annotations.SerializedName

data class Report(
    @SerializedName("reportType") val reportType: String,
    @SerializedName("reason") val reason : String?,
    @SerializedName("idx") val Idx : Int,
    @SerializedName("content") val content : String?
)


data class ReportResponse(
    @SerializedName("reportedUserIdx") val reportedUserIdx: Int,
    @SerializedName("patchModifyScoreRes") val result : PatchModifyScoreRes,
)

data class PatchModifyScoreRes(
    @SerializedName("type") val type: String,
    @SerializedName("status") val status : String?,
    @SerializedName("levelChanged") val levelChanged : Boolean,
)
