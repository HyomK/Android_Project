package com.likefirst.btos.utils

import com.likefirst.btos.data.entities.UserSign
import com.likefirst.btos.data.remote.users.response.GetProfileResponse
import com.likefirst.btos.data.remote.users.response.LoginResponse
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
    fun signUp(@Body user: UserSign) : Call<LoginResponse>

    @GET("/users/{useridx}")
    fun getProfile(@Path("useridx") useridx: Int): Call<GetProfileResponse>
}