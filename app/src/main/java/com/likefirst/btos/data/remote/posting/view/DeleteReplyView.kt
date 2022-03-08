package com.likefirst.btos.data.remote.posting.view

interface DeleteReplyView {
    fun onDeleteReplySuccess()
    fun onDeleteReplyFailure(code : Int, message : String)
}