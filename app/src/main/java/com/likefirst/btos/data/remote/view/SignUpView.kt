package com.likefirst.btos.data.remote.view

import com.likefirst.btos.data.remote.response.Login

interface SignUpView {
    fun onSignUpLoading()
    fun onSignUpSuccess(login : Login)
    fun onSignUpFailure(code : Int, message : String)
}