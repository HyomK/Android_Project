package com.likefirst.btos.data.remote.users.view

import com.likefirst.btos.data.remote.users.response.Login

interface AutoLoginView {
    fun onAutoLoginLoading()
    fun onAutoLoginSuccess(login : Login)
    fun onAutoLoginFailure(code : Int, message : String)
}