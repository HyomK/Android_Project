package com.likefirst.btos.data.module

import com.google.gson.annotations.SerializedName
import com.likefirst.btos.data.module.Plant
import io.reactivex.Single
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

data class PlantRequest(val userId : Int, val plantId:Int){
    @SerializedName("userIdx") val userIdx : Int = userId
    @SerializedName("plantIdx") val plantIdx :Int =plantId
}


data class PlantResponse(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code : Int,
    @SerializedName("message") val message : String,
    @SerializedName("result") val result : ArrayList<Plant>?
)

interface ApiInterface {
    @FormUrlEncoded
    @GET("/plants/{userId}")
    fun loadPlantList(
        @Path("userId") id: String
    ): Single<PlantResponse>
    @FormUrlEncoded
    @PATCH("/plants/select")
    fun selectPlant(
        @Body PlantSelectRequest : PlantRequest
    ): Single<PlantResponse>

    @FormUrlEncoded
    @POST("/plants/buy")
    fun buyPlant(
        @Body PlantBuyRequest : PlantRequest
    ):Single<PlantResponse>

    companion object {
        private const val DEV_URL: String = "https://dev.euna.shop";       // 테스트 서버 주소
        private  const val PROD_URL: String = "https://prod.euna.shop"    // 실서버 주소
        private const val BASE_URL: String = DEV_URL

        fun create(): ApiInterface {
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
                .create(ApiInterface::class.java)
        }
    }
}