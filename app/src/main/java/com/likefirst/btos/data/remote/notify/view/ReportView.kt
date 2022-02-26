package com.likefirst.btos.data.remote.notify.view

import com.likefirst.btos.data.remote.notify.response.PatchModifyScoreRes
import com.likefirst.btos.data.remote.notify.response.ReportResponse

interface ReportView {
    fun onReportSuccess(response: ReportResponse)
    fun onReportFailure(code : Int, message : String)
}