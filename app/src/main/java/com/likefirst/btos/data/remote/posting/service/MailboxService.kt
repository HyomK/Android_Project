package com.likefirst.btos.data.remote.posting.service

import android.util.Log
import com.likefirst.btos.ApplicationClass
import com.likefirst.btos.data.remote.posting.response.MailboxResponse
import com.likefirst.btos.data.remote.posting.view.MailDiaryView
import com.likefirst.btos.data.remote.posting.view.MailLetterView
import com.likefirst.btos.data.remote.posting.view.MailReplyView
import com.likefirst.btos.data.remote.posting.view.MailboxView
import com.likefirst.btos.utils.RetrofitInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MailboxService() {
    private lateinit var mailboxView: MailboxView

    private val mailboxService= ApplicationClass.retrofit.create(RetrofitInterface::class.java)

    fun setMailboxView(mailboxView: MailboxView){
        this.mailboxView=mailboxView
    }

    fun loadMailbox(userId: Int){

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

            }
        })
    }


}