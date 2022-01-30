package com.likefirst.btos.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.internal.OnConnectionFailedListener
import com.google.android.gms.tasks.Task
import com.likefirst.btos.R
import com.likefirst.btos.data.entities.User
import com.likefirst.btos.data.local.UserDatabase
import com.likefirst.btos.data.remote.response.Login
import com.likefirst.btos.data.remote.service.AuthService
import com.likefirst.btos.data.remote.view.AutoLoginView
import com.likefirst.btos.data.remote.view.GetProfileView
import com.likefirst.btos.data.remote.view.LoginView
import com.likefirst.btos.databinding.ActivityLoginBinding
import com.likefirst.btos.ui.BaseActivity
import com.likefirst.btos.ui.main.MainActivity
import com.likefirst.btos.utils.getJwt
import com.likefirst.btos.utils.saveJwt

class LoginActivity
    : BaseActivity<ActivityLoginBinding>(ActivityLoginBinding::inflate), OnConnectionFailedListener, LoginView, AutoLoginView, GetProfileView {
    val G_SIGN_IN : Int = 1
    lateinit var googleSignInClient: GoogleSignInClient
    lateinit var email : String
    val authService = AuthService()

    override fun initAfterBinding() {

        val animFadeOut = AnimationUtils.loadAnimation(applicationContext, R.anim.fade_out)
        val animFadeIn = AnimationUtils.loadAnimation(applicationContext, R.anim.fade_in)

        // animation_logo_FadeOut
        Handler(Looper.getMainLooper()).postDelayed({
            binding.loginLogoIv.visibility = View.VISIBLE
            binding.loginLogoIv.startAnimation(animFadeOut)

            //자동로그인
            authService.setAutoLoginView(this)
            Log.e("AUTOLOGIN/JWT",getJwt().toString())
            if(getJwt()!=null)
                authService.autologin()
            else{
                // animation_loginText_FadeIn
                binding.loginWelcomeTv.visibility = View.VISIBLE
                binding.loginWelcomeTv.startAnimation(animFadeIn)
                binding.loginGoogleLoginTv.visibility = View.VISIBLE
                binding.loginGoogleLoginTv.startAnimation(animFadeIn)
            }

        },3000)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        binding.loginGoogleLoginTv.setOnClickListener{
            var signInIntent : Intent = googleSignInClient.signInIntent
            startActivityForResult(signInIntent, G_SIGN_IN)
        }
    }

    override fun onConnectionFailed(p0: ConnectionResult) {

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == G_SIGN_IN){
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)

            val account = task.getResult(ApiException::class.java)
            email = account?.email.toString()
            Log.e("account", email)

            authService.setLoginView(this)
            authService.login(email)
        }
    }

    override fun onLoginLoading() {
        binding.loginLoadingPb.visibility = View.VISIBLE
    }

    override fun onLoginSuccess(login: Login) {
        binding.loginLoadingPb.visibility = View.GONE

        saveJwt(login.jwt!!)
        Log.e("LOGIN/JWT", getJwt()!!)

        //프로필 정보 가져와서 userdb에 저장
        authService.setGetProfileView(this)
        authService.getProfile(login.userIdx)

        val intent = Intent(this, MainActivity::class.java)
        finish()
        startActivity(intent)
    }

    override fun onLoginFailure(code: Int, message: String) {
        binding.loginLoadingPb.visibility = View.GONE

        when(code){
            4000 -> {
                Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
                Log.e("LOGIN/FAIL", message)
            }
            5003 -> {
                Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
                Log.e("LOGIN/FAIL", message)
                val intent = Intent(this, OnboardingActivity::class.java)
                val bundle = Bundle()
                bundle.putString("email",email)
                intent.putExtra("mypackage",bundle)
                finish()
                startActivity(intent)
            }
        }
    }

    override fun onAutoLoginLoading() {
        binding.loginLoadingPb.visibility = View.VISIBLE
    }

    override fun onAutoLoginSuccess(login : Login) {
        binding.loginLoadingPb.visibility = View.GONE

        //프로필 정보 가져와서 userdb에 저장
        authService.setGetProfileView(this)
        authService.getProfile(login.userIdx)

        val intent = Intent(this, MainActivity::class.java)
        finish()
        startActivity(intent)
    }

    override fun onAutoLoginFailure(code: Int, message: String) {
        binding.loginLoadingPb.visibility = View.GONE
    }

    override fun onGetProfileViewLoading() {
    }

    override fun onGetProfileViewSuccess(user: User) {
        //UserDB에 프로필 정보 저장
        val userDB = UserDatabase.getInstance(this)?.userDao()
        userDB?.insert(user)
        Log.e("PROFILE/API",userDB?.getUser().toString())
    }

    override fun onGetProfileViewFailure(code: Int, message: String) {

    }
}