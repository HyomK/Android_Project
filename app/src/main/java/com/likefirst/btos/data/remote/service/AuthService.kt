package com.likefirst.btos.data.remote.service

import android.util.Log
import com.likefirst.btos.ApplicationClass.Companion.retrofit
import com.likefirst.btos.data.remote.response.LoginResponse
import com.likefirst.btos.data.remote.view.AutoLoginView
import com.likefirst.btos.data.remote.view.LoginView
import com.likefirst.btos.utils.RetrofitInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthService {

    private lateinit var loginView : LoginView
    private lateinit var autologinView : AutoLoginView

    private val AuthService = retrofit.create(RetrofitInterface::class.java)

    fun setLoginView(loginView : LoginView){
        this.loginView = loginView
    }

    fun setAutoLoginView(autologinView : AutoLoginView){
        this.autologinView = autologinView
    }

    fun login(email : String){

        loginView.onLoginLoading()

        AuthService.login(email).enqueue(object: Callback<LoginResponse> {

            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {

                val LoginResponse: LoginResponse = response.body()!!

                Log.e("LOGIN/API",LoginResponse.toString())

                when(LoginResponse.code){
                    1000 -> loginView.onLoginSuccess(LoginResponse.result)
                    else -> loginView.onLoginFailure(LoginResponse.code, LoginResponse.message)
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                loginView.onLoginFailure(4000, "데이터베이스 연결에 실패하였습니다.")
                loginView.onLoginFailure(5003, "회원가입이 필요합니다.")
            }

        })
    }

    fun autologin(jwt : String){
        autologinView.onAutoLoginLoading()

        AuthService.autoLogin(jwt).enqueue(object : Callback<LoginResponse>{
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {

                val autoLoginResponse: LoginResponse = response.body()!!

                Log.e("AUTOLOGIN/API",autoLoginResponse.toString())

                when(autoLoginResponse.code){
                    1000 -> autologinView.onAutoLoginSuccess()
                    else -> autologinView.onAutoLoginFailure(autoLoginResponse.code,autoLoginResponse.message)
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                autologinView.onAutoLoginFailure(400,"네트워크 오류가 발생했습니다.")
            }
        })
    }
}