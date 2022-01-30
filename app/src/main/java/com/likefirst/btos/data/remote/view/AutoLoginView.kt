package com.likefirst.btos.data.remote.view

import com.likefirst.btos.data.remote.response.Login

interface AutoLoginView {
    fun onAutoLoginLoading()
    fun onAutoLoginSuccess(login : Login)
    fun onAutoLoginFailure(code : Int, message : String)
}