package com.likefirst.btos.data.remote.view

interface AutoLoginView {
    fun onAutoLoginLoading()
    fun onAutoLoginSuccess()
    fun onAutoLoginFailure(code : Int, message : String)
}