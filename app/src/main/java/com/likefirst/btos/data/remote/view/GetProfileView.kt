package com.likefirst.btos.data.remote.view

import com.likefirst.btos.data.entities.User

interface GetProfileView {
    fun onGetProfileViewLoading()
    fun onGetProfileViewSuccess(user : User)
    fun onGetProfileViewFailure(code : Int, message : String)
}