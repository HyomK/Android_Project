package com.likefirst.btos.ui.main

import android.content.Intent
import android.os.Handler
import android.os.Looper
import com.likefirst.btos.databinding.ActivitySplashBinding
import com.likefirst.btos.ui.BaseActivity

class SplashActivity : BaseActivity<ActivitySplashBinding> (ActivitySplashBinding::inflate) {
    override fun initAfterBinding() {
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this,LoginActivity::class.java)
            finish()
            startActivity(intent)
        },5000)
    }


}