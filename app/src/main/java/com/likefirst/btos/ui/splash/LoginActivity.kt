package com.likefirst.btos.ui.splash

import android.content.Intent
import android.util.Log
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.internal.OnConnectionFailedListener
import com.google.android.gms.tasks.Task
import com.likefirst.btos.databinding.ActivityLoginBinding
import com.likefirst.btos.ui.BaseActivity

class LoginActivity : BaseActivity<ActivityLoginBinding>(ActivityLoginBinding::inflate), OnConnectionFailedListener {
    val G_SIGN_IN : Int = 1


    override fun initAfterBinding() {

        lateinit var googleSignInClient: GoogleSignInClient

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

//        binding.loginGoogleLoginTv.setSize(SignInButton.SIZE_STANDARD)


        binding.loginGoogleLoginTv.setOnClickListener{
            var signInIntent : Intent = googleSignInClient.signInIntent
            startActivityForResult(signInIntent, G_SIGN_IN)
        }
        
        //자동로그인
//        val account = GoogleSignIn.getLastSignedInAccount(this)
//        if(account==null){
//            //자동 로그인 X
//        }else{
//            //자동 로그인
//        }
    }

    override fun onConnectionFailed(p0: ConnectionResult) {

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == G_SIGN_IN){
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
            val intent = Intent(this, OnboardingActivity::class.java)
            finish()
            startActivity(intent)
        }
    }

    private fun handleSignInResult(task: Task<GoogleSignInAccount>) {
        try{
            val account = task.getResult(ApiException::class.java)
            val email = account?.email.toString()
            val familyName = account?.familyName.toString()
            val givenName = account?.givenName.toString()
            val displayName = account?.displayName.toString()

            Log.d("account", email)
            Log.d("account", familyName)
            Log.d("account", givenName)
            Log.d("account", displayName)
        }catch (e: ApiException){
            Log.w("Failed AT ", "signInResult:failed code=" + e.statusCode)
        }
    }
}