package com.likefirst.btos.data.remote.notify.view

interface FcmTokenView {
    fun onLoadingFcmToken()
    fun onSuccessFcmToken()
    fun onFailureFcmToken(code: Int, msg: String)
}