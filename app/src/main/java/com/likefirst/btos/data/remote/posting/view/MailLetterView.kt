package com.likefirst.btos.data.remote.posting.view

import com.likefirst.btos.data.remote.posting.response.MailInfoResponse



interface MailLetterView {
    fun onLetterLoading()
    fun onLetterSuccess(letter : MailInfoResponse)
    fun onLetterFailure(code : Int, message : String)
}