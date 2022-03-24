package com.likefirst.btos

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.likefirst.btos.config.XAccessTokenInterceptor
import dagger.hilt.android.HiltAndroidApp
import io.reactivex.exceptions.UndeliverableException
import io.reactivex.plugins.RxJavaPlugins
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.net.SocketException
import java.util.concurrent.TimeUnit


@HiltAndroidApp
class ApplicationClass : Application() {
    companion object{
        const val X_ACCESS_TOKEN: String = "x-access-token"         // JWT Token Key
        const val TAG: String = "BTOS-APP"                      // Log, SharedPreference
        const val APP_DATABASE = "$TAG-DB"

        const val DEV_URL: String = "https://dev.euna.shop";       // 테스트 서버 주소
        const val PROD_URL: String = "https://prod.euna.shop"    // 실서버 주소
        const val BASE_URL: String =PROD_URL

        lateinit var mSharedPreferences: SharedPreferences
        lateinit var retrofit: Retrofit
    }

    override fun onCreate() {
        super.onCreate()

        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

        val client: OkHttpClient = OkHttpClient.Builder()
            .readTimeout(30000, TimeUnit.MILLISECONDS)
            .connectTimeout(30000, TimeUnit.MILLISECONDS)
            .addInterceptor(loggingInterceptor)
            .addNetworkInterceptor(XAccessTokenInterceptor()) // JWT 자동 헤더 전송
            .build()

        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        mSharedPreferences = applicationContext.getSharedPreferences(TAG, Context.MODE_PRIVATE)

        RxJavaPlugins.setErrorHandler{
            var error = it
            if(error is UndeliverableException){
                error = it.cause
            }
            if( error is IOException || error is SocketException){
                return@setErrorHandler
            }
            if(error is InterruptedException){
                return@setErrorHandler
            }
            if(error is NullPointerException || error is IllegalArgumentException){
                Thread.currentThread().uncaughtExceptionHandler
                    .uncaughtException(Thread.currentThread(), error)
            }
            if(error is IllegalStateException){
                Thread.currentThread().uncaughtExceptionHandler
                    .uncaughtException(Thread.currentThread(), error)
                return@setErrorHandler
            }
        }
    }
}