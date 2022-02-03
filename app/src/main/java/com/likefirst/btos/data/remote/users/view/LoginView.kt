package com.likefirst.btos.data.remote.users.view

import com.likefirst.btos.data.remote.response.Login

interface LoginView {
    fun onLoginLoading()
    fun onLoginSuccess(login : Login)
    fun onLoginFailure(code : Int, message : String)
}