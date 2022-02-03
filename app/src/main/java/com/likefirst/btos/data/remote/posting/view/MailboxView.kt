package com.likefirst.btos.data.remote.posting.view

import com.likefirst.btos.data.remote.posting.response.Mailbox

interface MailboxView {
    fun onMailboxLoading()
    fun onMailboxSuccess(mailboxList : ArrayList<Mailbox>)
    fun onMailboxFailure(code : Int, message : String)
}