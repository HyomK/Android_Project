package com.likefirst.btos.data.remote.posting.view

import com.likefirst.btos.data.remote.posting.response.Letter


interface LetterView {
    fun onLetterLoading()
    fun onLetterSuccess(letter :Letter )
    fun onLetterFailure(code : Int, message : String)
}