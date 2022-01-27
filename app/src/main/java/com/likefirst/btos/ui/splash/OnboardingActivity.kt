package com.likefirst.btos.ui.splash

import android.content.Intent
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import com.likefirst.btos.R
import com.likefirst.btos.data.remote.response.Login
import com.likefirst.btos.data.remote.view.SignUpView
import com.likefirst.btos.databinding.ActivityOnboardingBinding
import com.likefirst.btos.ui.BaseActivity

class OnboardingActivity :BaseActivity<ActivityOnboardingBinding> ( ActivityOnboardingBinding::inflate), SignUpView{

    override fun initAfterBinding() {

        val imm: InputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        val agelist = resources.getStringArray(R.array.onboarding_agelist)
        val arrayAdapter = ArrayAdapter(this, R.layout.onboarding_dropdown_item,agelist)
        binding.onboardingAgelist.setAdapter(arrayAdapter)
        binding.onboardingAgelist.setDropDownBackgroundDrawable(resources.getDrawable(R.drawable.onboarding_age_box))
        binding.onboardingAgelist.dropDownHeight=400

        //나이 선택 시 키보드 내리기
        binding.onboardingAgelist.setOnClickListener {
            imm.hideSoftInputFromWindow(binding.onboardingNameEt.getWindowToken(), 0);
        }

        binding.onboardingOkayTv.setOnClickListener {
            // loginactivity에서 넘어온 email 받기
//            val intent = getIntent()
//            val email = intent.getStringExtra("email").toString()

            val nickname = binding.onboardingNameEt.text.toString()
            val birth = binding.onboardingAgelist.text.toString().toInt()
            Log.e("SIGNUP", "email:\nnickname:$nickname\nbirth:$birth")

            val intent = Intent(this,TutorialActivity::class.java)
            finish()
            startActivity(intent)

//            val authService = AuthService()
//            authService.setSignUpView(this)
//            authService.signUp(User(email,nickname,birth))
        }
    }

    override fun onSignUpLoading() {
        binding.onboardingLoadingPb.visibility = View.VISIBLE
    }

    override fun onSignUpSuccess(login: Login) {
        binding.onboardingLoadingPb.visibility = View.GONE

        Log.e("SIGNUP/userIdx", login.userIdx.toString())
        val intent = Intent(this, LoginActivity::class.java)
        finish()
        startActivity(intent)
    }

    override fun onSignUpFailure(code: Int, message: String) {
        binding.onboardingLoadingPb.visibility = View.GONE

        when(code){
            4000 -> {
                Log.e("SIGNUP/FAIL", message)
            }
        }
    }
}