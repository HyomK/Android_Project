package com.likefirst.btos.data.remote.users.view

import com.likefirst.btos.data.remote.users.response.Login

interface SignUpView {
    fun onSignUpLoading()
    fun onSignUpSuccess(login : Login)
    fun onSignUpFailure(code : Int, message : String)
}