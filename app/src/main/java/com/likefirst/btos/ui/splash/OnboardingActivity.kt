package com.likefirst.btos.ui.splash

import android.content.Intent
import android.widget.ArrayAdapter
import com.likefirst.btos.R
import com.likefirst.btos.databinding.ActivityOnboardingBinding
import com.likefirst.btos.ui.BaseActivity

class OnboardingActivity :BaseActivity<ActivityOnboardingBinding> ( ActivityOnboardingBinding::inflate){
    override fun initAfterBinding() {

        val agelist = resources.getStringArray(R.array.onboarding_agelist)
        val arrayAdapter = ArrayAdapter(this, R.layout.onboarding_dropdown_item,agelist)
        binding.onboardingAgelist.setAdapter(arrayAdapter)
        binding.onboardingAgelist.setDropDownBackgroundDrawable(resources.getDrawable(R.drawable.onboarding_age_box))
        binding.onboardingAgelist.dropDownHeight=350

        binding.onboardingOkayTv.setOnClickListener {
            val intent = Intent(this, TutorialActivity::class.java)
            finish()
            startActivity(intent)
        }
    }
}