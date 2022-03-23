package com.likefirst.btos.data.remote.posting

import com.likefirst.btos.ApplicationClass
import com.likefirst.btos.data.remote.plant.PlantRequest
import com.likefirst.btos.data.remote.plant.PlantResponse
import com.likefirst.btos.data.remote.BaseResponse
import com.likefirst.btos.data.remote.posting.response.MailInfoResponse
import com.likefirst.btos.data.remote.posting.response.MailboxResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface MailboxApiInterface {
    @GET("/mailboxes")
    fun loadMailbox(
        @Query("userIdx") id: Int
    ): Response<MailboxResponse>

    @GET("/mailboxes/mail")
    fun loadDiary(
        @Query("userIdx")userIdx: Int,
        @Query("type") type: String = "diary",
        @Query("typeIdx")idx:Int
    ): Response<BaseResponse<MailInfoResponse>>

    @GET("/mailboxes/mail")
    fun loadLetter(
        @Query("userIdx")  userIdx: Int,
        @Query("type") type: String = "letter",
        @Query("typeIdx")idx:Int
    ): Response<BaseResponse<MailInfoResponse>>


    @GET("/mailboxes/mail")
    fun loadReply(
        @Query("userIdx") userIdx: Int,
        @Query("type") type: String = "reply",
        @Query("typeIdx")idx:Int
    ): Response<BaseResponse<MailInfoResponse>>


    @GET("/mailboxes")
    suspend fun getMailbox(
        @Query("userIdx") id: Int
    ): Response<MailboxResponse>


    companion object {
        private const val BASE_URL: String = ApplicationClass.BASE_URL

        fun create(): HistoryApi {
            val logger = HttpLoggingInterceptor().apply {
                level =
                    HttpLoggingInterceptor.Level.BASIC
            }
            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(HistoryApi::class.java)
        }
    }
}