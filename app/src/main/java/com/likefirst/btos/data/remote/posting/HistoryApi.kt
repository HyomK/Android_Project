package com.likefirst.btos.data.remote.posting

import com.likefirst.btos.ApplicationClass
import com.likefirst.btos.data.entities.BasicHistory
import com.likefirst.btos.data.entities.Content
import com.likefirst.btos.data.entities.SenderList
import com.likefirst.btos.data.remote.history.response.HistoryBaseResponse
import com.likefirst.btos.data.remote.history.response.HistoryDetailResponse
import com.likefirst.btos.data.remote.history.response.HistorySenderDetailResponse
import com.likefirst.btos.data.remote.plant.PlantApiInterface
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface HistoryApi {
    @GET("/histories/list/{userIdx}/{pageNum}")
    suspend fun historyListSender(
        @Path("userIdx") userIdx : Int,
        @Path("pageNum") pageNum : Int,
        @Query("filtering") filtering: String,
        @Query("search") search: String?
    ) : Response<HistoryBaseResponse<BasicHistory<SenderList>>>
    @GET("/histories/list/{userIdx}/{pageNum}")
    suspend fun historyListDiaryLetter(
        @Path("userIdx") userIdx : Int,
        @Path("pageNum") pageNum : Int,
        @Query("filtering") filtering : String,
        @Query("search") search : String?
    ) : Response<HistoryBaseResponse<BasicHistory<Content>>>
    @GET("/histories/sender/{userIdx}/{senderNickName}/{pageNum}")
    suspend fun historyListSenderDetail(
        @Path("userIdx") userIdx : Int,
        @Path("senderNickName") senderNickName : String,
        @Path("pageNum") pageNum : Int,
        @Query("search") search : String?
    ) : Response<HistorySenderDetailResponse>
    @GET("/histories/{userIdx}/{type}/{typeIdx}")
    suspend fun historyDetailList(
        @Path("userIdx") userIdx : Int,
        @Path("type") type : String,
        @Path("typeIdx") typeIdx : Int,
    ) : Response<HistoryDetailResponse>

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