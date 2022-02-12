package com.likefirst.btos.data.remote.posting.view

import com.likefirst.btos.data.remote.posting.response.MailLetterDetailResponse


interface MailLetterView {
    fun onLetterLoading()
    fun onLetterSuccess(letter :MailLetterDetailResponse )
    fun onLetterFailure(code : Int, message : String)
}