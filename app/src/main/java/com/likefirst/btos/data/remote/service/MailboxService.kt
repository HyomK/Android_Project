package com.likefirst.btos.data.remote.service

import android.util.Log
import com.likefirst.btos.ApplicationClass.Companion.retrofit
import com.likefirst.btos.ApplicationClass
import com.likefirst.btos.data.remote.response.Mailbox
import com.likefirst.btos.data.remote.response.MailboxResponse
import com.likefirst.btos.data.remote.view.MailboxView
import com.likefirst.btos.utils.RetrofitInterface
import com.likefirst.btos.data.entities.User
import com.likefirst.btos.data.remote.view.DiaryView
import com.likefirst.btos.data.remote.view.LetterView
import com.likefirst.btos.data.remote.view.ReplyView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MailboxService {
    private lateinit var mailboxView: MailboxView
    private lateinit var diaryView: DiaryView
    private lateinit var letterView:LetterView
    private lateinit var replyView:ReplyView


    private val mailboxService= ApplicationClass.retrofit.create(RetrofitInterface::class.java)

    fun setMailboxView(mailboxView: MailboxView){
        this.mailboxView=mailboxView
    }

    fun loadMailbox(userId: String){

        mailboxView.onMailboxLoading()

        mailboxService.loadMailbox(userId).enqueue(object: Callback<MailboxResponse>{

            override fun onResponse(
                call: Call<MailboxResponse>,
                response: Response<MailboxResponse>
            ) {
                val mailboxResponse: MailboxResponse=response.body()!!
                Log.e("MailBox/API", mailboxResponse.toString())

                when(mailboxResponse.code){
                    1000->mailboxView.onMailboxSuccess(mailboxResponse.result)
                    else->mailboxView.onMailboxFailure(mailboxResponse.code, mailboxResponse.message)
                }
            }

            override fun onFailure(call: Call<MailboxResponse>, t: Throwable) {
                mailboxView.onMailboxFailure(4000,"데이터베이스 연결에 실패하였습니다.")
            }
        })


    }

    fun loadDiary(userId: String){
        mailboxView.onMailboxLoading()
    }

}