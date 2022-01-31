package com.likefirst.btos.data.remote.service

import android.util.Log
import com.likefirst.btos.ApplicationClass.Companion.retrofit
import com.likefirst.btos.data.entities.UserSign
import com.likefirst.btos.data.remote.response.GetProfileResponse
import com.likefirst.btos.data.remote.response.LoginResponse
import com.likefirst.btos.data.remote.view.AutoLoginView
import com.likefirst.btos.data.remote.view.GetProfileView
import com.likefirst.btos.data.remote.view.LoginView
import com.likefirst.btos.data.remote.view.SignUpView
import com.likefirst.btos.utils.RetrofitInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthService {

    private lateinit var loginView : LoginView
    private lateinit var autologinView : AutoLoginView
    private lateinit var signupView: SignUpView
    private lateinit var getprofileView: GetProfileView

    private val AuthService = retrofit.create(RetrofitInterface::class.java)

    fun setLoginView(loginView : LoginView){
        this.loginView = loginView
    }

    fun setAutoLoginView(autologinView : AutoLoginView){
        this.autologinView = autologinView
    }

    fun setSignUpView(signupView : SignUpView){
        this.signupView = signupView
    }

    fun setGetProfileView(getprofileView: GetProfileView){
        this.getprofileView = getprofileView
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

    fun autologin(){
        autologinView.onAutoLoginLoading()

        AuthService.autoLogin().enqueue(object : Callback<LoginResponse>{
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {

                val autoLoginResponse: LoginResponse = response.body()!!

                Log.e("AUTOLOGIN/API",autoLoginResponse.toString())

                when(autoLoginResponse.code){
                    1000 -> autologinView.onAutoLoginSuccess(autoLoginResponse.result)
                    else -> autologinView.onAutoLoginFailure(autoLoginResponse.code,autoLoginResponse.message)
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                autologinView.onAutoLoginFailure(400,"네트워크 오류가 발생했습니다.")
            }
        })
    }

    fun signUp(user: UserSign){
        signupView.onSignUpLoading()

        AuthService.signUp(user).enqueue(object : Callback<LoginResponse>{
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                val signUpResponse: LoginResponse = response.body()!!

                Log.e("SIGNUP/API",signUpResponse.toString())

                when(signUpResponse.code){
                    1000 -> signupView.onSignUpSuccess(signUpResponse.result)
                    else -> signupView.onSignUpFailure(signUpResponse.code,signUpResponse.message)
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                signupView.onSignUpFailure(400,"네트워크 오류가 발생했습니다.")
            }

        })
    }

    fun getProfile(useridx: Int){
        getprofileView.onGetProfileViewLoading()

        AuthService.getProfile(useridx).enqueue(object : Callback<GetProfileResponse>{
            override fun onResponse(call: Call<GetProfileResponse>, response: Response<GetProfileResponse>) {
                val getProfileResponse : GetProfileResponse = response.body()!!

                when(getProfileResponse.code){
                    1000 -> getprofileView.onGetProfileViewSuccess(getProfileResponse.result)
                    else -> getprofileView.onGetProfileViewFailure(getProfileResponse.code,getProfileResponse.message)
                }
            }

            override fun onFailure(call: Call<GetProfileResponse>, t: Throwable) {
                getprofileView.onGetProfileViewFailure(400, "네트워크 오류가 발생했습니다.")
            }

        })
    }
}