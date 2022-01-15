package com.likefirst.btos.ui.main

import android.content.Intent
import com.likefirst.btos.databinding.ActivityLoginBinding
import com.likefirst.btos.ui.BaseActivity

class LoginActivity : BaseActivity<ActivityLoginBinding>(ActivityLoginBinding::inflate){
    override fun initAfterBinding() {

        binding.loginGoogleloginTv.setOnClickListener {
            val intent = Intent(this,OnboardingActivity::class.java)
            finish()
            startActivity(intent)
        }

    }
}