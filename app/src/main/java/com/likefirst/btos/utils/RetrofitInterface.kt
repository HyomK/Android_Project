package com.likefirst.btos.utils

import com.likefirst.btos.data.entities.User
import com.likefirst.btos.data.remote.response.*
import retrofit2.Call
import retrofit2.http.*

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

    @GET("/mailboxes/mail")
    fun loadDiary(
        @Query("type") type: String,
        @Query("idx")idx: String
    ): Call<DiaryResponse>

    @GET("/mailboxes/mail")
    fun loadLetter(
        @Query("type") type: String,
        @Query("idx")idx: String
    ): Call<LetterResponse>


    @GET("/mailboxes/mail")
    fun loadReply(
        @Query("type") type: String,
        @Query("idx")idx: String
    ): Call<ReplyResponse>

    // -------------------PlantList-------------------------- //

    @GET("/plant/{userId}/list")
    fun loadPlantList(
        @Path("userId") id: String
    ): Call<PlantResponse>

    @PATCH("/plant/{userId}/select")
    fun selectPlant(
        @Query("userId") userId: String,
        @Query("PlantId") PlantId:String
    ): Call<PlantResponse>

    @POST("/plant/{userId}/buy")
    fun buyPlant(
        @Query("userId") userId: String,
        @Query("PlantId") PlantId:String
    ): Call<PlantResponse>


}