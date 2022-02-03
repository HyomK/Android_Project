package com.likefirst.btos.utils

import com.likefirst.btos.utils.Constants.Companion.FCM_URL
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okio.IOException
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object FirebaseRetrofit {
    private val FCM_KEY = "AAAA4ageO_g:APA91bGMNBpRuyibDVwa9RptTRXZb5wFmMXk1Z9VE_tVyb0zl5re63CGQUgudTuXBaJnXTQzP__2m-YwSQ7Mefca20Fo_OWaRK23NMGKUQhGtwtP3kblibvqHapWqlYaptwhYXIqqhBw"
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(FCM_URL)
            .client(provideOkHttpClient(AppInterceptor()))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api : FcmInterface by lazy {
        retrofit.create(FcmInterface::class.java)
    }

    // Client
    private fun provideOkHttpClient(
        interceptor: AppInterceptor
    ): OkHttpClient = OkHttpClient.Builder()
        .run {
            addInterceptor(interceptor)
            build()
        }

    // 헤더 추가
    class AppInterceptor : Interceptor {
        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain)
                : okhttp3.Response = with(chain) {
            val newRequest = request().newBuilder()
                .addHeader("Authorization", "key=$FCM_KEY")
                .addHeader("Content-Type", "application/json")
                .build()
            proceed(newRequest)
        }
    }
}