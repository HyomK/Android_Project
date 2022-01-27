package com.likefirst.btos.ui.splash

import android.content.Intent
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
import com.likefirst.btos.data.remote.response.Login
import com.likefirst.btos.data.remote.service.AuthService
import com.likefirst.btos.data.remote.view.LoginView
import com.likefirst.btos.databinding.ActivityLoginBinding
import com.likefirst.btos.ui.BaseActivity
import com.likefirst.btos.ui.main.MainActivity
import com.likefirst.btos.utils.getJwt
import com.likefirst.btos.utils.saveJwt

class LoginActivity
    : BaseActivity<ActivityLoginBinding>(ActivityLoginBinding::inflate), OnConnectionFailedListener, LoginView {
    val G_SIGN_IN : Int = 1
    lateinit var googleSignInClient: GoogleSignInClient
    lateinit var email : String

    override fun initAfterBinding() {

        val animFadeOut = AnimationUtils.loadAnimation(applicationContext, R.anim.fade_out)
        val animFadeIn = AnimationUtils.loadAnimation(applicationContext, R.anim.fade_in)

        // animation_logo_FadeOut
        Handler(Looper.getMainLooper()).postDelayed({
            binding.loginLogoIv.visibility = View.VISIBLE
            binding.loginLogoIv.startAnimation(animFadeOut)
        },5000)

        // animation_loginText_FadeIn
        Handler(Looper.getMainLooper()).postDelayed({
            binding.loginWelcomeTv.visibility = View.VISIBLE
            binding.loginWelcomeTv.startAnimation(animFadeIn)
            binding.loginGoogleLoginTv.visibility = View.VISIBLE
            binding.loginGoogleLoginTv.startAnimation(animFadeIn)
        },7000)

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

            val authService = AuthService()
            authService.setLoginView(this)

            authService.login(email)
        }
    }

//    private fun handleSignInResult(task: Task<GoogleSignInAccount>) {
//        try{
//
//            val familyName = account?.familyName.toString()
//            val givenName = account?.givenName.toString()
//            val displayName = account?.displayName.toString()
//
//            Log.d("account", familyName)
//            Log.d("account", givenName)
//            Log.d("account", displayName)
//        }catch (e: ApiException){
//            Log.w("Failed AT ", "signInResult:failed code=" + e.statusCode)
//        }
//    }

    override fun onLoginLoading() {
        binding.loginLoadingPb.visibility = View.VISIBLE
    }

    override fun onLoginSuccess(login: Login) {
        binding.loginLoadingPb.visibility = View.GONE
        Log.e("LOGIN/JWT", "SUCCESS~!")
        saveJwt(this,login.jwt!!)
        Log.e("LOGIN/JWT", getJwt(this)!!)
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
                finish()
                startActivity(intent)
            }
        }
    }

//    override fun onAutoLoginLoading() {
//        binding.loginLoadingPb.visibility = View.VISIBLE
//    }
//
//    override fun onAutoLoginSuccess() {
//        binding.loginLoadingPb.visibility = View.GONE
//        val intent = Intent(this, MainActivity::class.java)
//        finish()
//        startActivity(intent)
//    }
//
//    override fun onAutoLoginFailure(code: Int, message: String) {
//        binding.loginLoadingPb.visibility = View.GONE
//
//        when(code){
//            2002, 2001-> {
//                val intent = Intent(this,LoginActivity::class.java)
//                finish()
//                startActivity(intent)
//            }
//        }
//    }
}