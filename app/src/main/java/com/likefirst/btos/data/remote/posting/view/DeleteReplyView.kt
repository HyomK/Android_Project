package com.likefirst.btos.data.remote.posting.view

interface DeleteReplyView {
    fun onDeleteReplyLoading()
    fun onDeleteReplySuccess()
    fun onDeleteReplyFailure(code : Int, message : String)
}