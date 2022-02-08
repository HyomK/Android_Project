package com.likefirst.btos.data.remote.users.view

interface SetSettingUserView {
    fun onSetSettingUserViewLoading()
    fun onSetSettingUserViewSuccess(result : String)
    fun onSetSettingUserViewFailure(code : Int, message : String)
}