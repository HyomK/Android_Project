package com.likefirst.btos.data.remote.posting.view

import com.likefirst.btos.data.remote.posting.response.MailLetterResponse


interface MailLetterView {
    fun onLetterLoading()
    fun onLetterSuccess(letter : MailLetterResponse)
    fun onLetterFailure(code : Int, message : String)
}