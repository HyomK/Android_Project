package com.likefirst.btos.utils


import com.likefirst.btos.data.entities.UserSign
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
    fun signUp(@Body user: UserSign) : Call<LoginResponse>

    @GET("/users/{useridx}")
    fun getProfile(@Path("useridx") useridx: Int): Call<GetProfileResponse>


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

    @GET("/plants/{userId}")
    fun loadPlantList(
        @Path("userId") id: String
    ): Call<PlantResponse>

    @PATCH("/plants/select")
    fun selectPlant(
        @Body PlantBuyRequest : PlantRequest
    ): Call<PlantResponse>


    @POST("/plants/buy")
    fun buyPlant(
        @Body PlantBuyRequest : PlantRequest
    ): Call<PlantResponse>

}