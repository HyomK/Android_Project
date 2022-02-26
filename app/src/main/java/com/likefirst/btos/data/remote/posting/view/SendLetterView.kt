package com.likefirst.btos.data.remote.posting.view

import com.likefirst.btos.data.remote.posting.response.MailInfoResponse

interface SendLetterView {
    fun onSendLetterLoading()
    fun onSendLetterSuccess()
    fun onSendLetterFailure(code : Int, message : String)
}