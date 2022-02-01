package com.likefirst.btos.data.remote.view.mailbox

import com.likefirst.btos.data.remote.response.Letter


interface LetterView {
    fun onLetterLoading()
    fun onLetterSuccess(letter :Letter )
    fun onLetterFailure(code : Int, message : String)
}