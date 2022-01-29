package com.likefirst.btos.data.remote.view

import com.likefirst.btos.data.remote.response.Letter


interface LetterView {
    fun onLetterLoading()
    fun onLetterSuccess(letterList : ArrayList<Letter> )
    fun onLetterFailure(code : Int, message : String)
}