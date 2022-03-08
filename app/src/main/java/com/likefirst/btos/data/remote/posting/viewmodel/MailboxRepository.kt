package com.likefirst.btos.data.remote.posting.viewmodel

import com.likefirst.btos.ApplicationClass
import com.likefirst.btos.utils.RetrofitInterface

class MailboxRepository  {
    private val mailboxService= ApplicationClass.retrofit.create(RetrofitInterface::class.java)

    suspend fun loadMailboxList(userIdx : Int) = mailboxService.getMailbox(userIdx) ;

    suspend fun loadDiary(userIdx: Int,typeIdx: Int) = mailboxService.loadDiary(userIdx,"diary",typeIdx)

    suspend fun loadLetter(userIdx: Int,typeIdx: Int) = mailboxService.loadLetter(userIdx,"letter",typeIdx)

    suspend fun loadReply(userIdx: Int,typeIdx: Int) = mailboxService.loadReply(userIdx,"reply",typeIdx)
}