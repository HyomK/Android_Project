package com.likefirst.btos.utils

import com.likefirst.btos.data.entities.User
import com.likefirst.btos.data.remote.response.*
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface RetrofitInterface {

    @POST("/auth/google")
    fun login(@Body email: String) : Call<LoginResponse>

    @GET("/auth/jwt")
    fun autoLogin() : Call<LoginResponse>
    //@Header("x-access-token") jwt: String
    @POST("/users")
    fun signUp(@Body user: User) : Call<LoginResponse>


    // -------------------Mailbox -------------------------- //
    @GET("/mailboxes/{userId}")
    fun loadMailbox(
        @Path("userId") id: String
    ): Call<MailboxResponse>

    @GET("/mailboxes/mail?type=diary&idx={userId}")
    fun loadDiary(
        @Path("userId") id: String,
    ): Call<DiaryResponse>

    @GET("/mailboxes/mail?type=letter&idx={userId}")
    fun loadLetter(
        @Path("userId") id: String,
    ): Call<LetterResponse>


    @GET("/mailboxes/mail?type=reply&idx={userId}")
    fun loadReply(
        @Path("userId") id: String,
    ): Call<ReplyResponse>


}