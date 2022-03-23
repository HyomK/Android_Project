package com.likefirst.btos.data.remote.plant

import com.google.gson.annotations.SerializedName
import com.likefirst.btos.ApplicationClass
import com.likefirst.btos.data.entities.Plant
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
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

interface PlantApiInterface {

    @GET("/plants/{userId}")
    suspend fun loadPlantList(
        @Path("userId") id: String
    ): Response<PlantResponse>

    @PATCH("/plants/select")
    suspend fun selectPlant(
        @Body PlantSelectRequest : PlantRequest
    ): Response<PlantResponse>


    @POST("/plants/buy")
    suspend fun buyPlant(
        @Body PlantBuyRequest : PlantRequest
    ):Response<PlantResponse>

    companion object {
        private const val BASE_URL: String = ApplicationClass.BASE_URL

        fun create(): PlantApiInterface {
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
                .create(PlantApiInterface::class.java)
        }
    }
}