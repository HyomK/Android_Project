package com.likefirst.btos.data.remote.users.view

import com.likefirst.btos.data.entities.UserIsSad

interface UpdateIsSadView {
    fun onUpdateLoading()
    fun onUpdateSuccess(isSad : UserIsSad)
    fun onUpdateFailure(code : Int, message : String)
}