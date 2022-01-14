package com.likefirst.btos.ui.main

import android.content.Intent
import com.likefirst.btos.databinding.ActivityOnboardingBinding
import com.likefirst.btos.ui.BaseActivity

class OnboardingActivity :BaseActivity<ActivityOnboardingBinding> ( ActivityOnboardingBinding::inflate){
    override fun initAfterBinding() {

        binding.onboardingOkayTv.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            finish()
            startActivity(intent)
        }
    }
}