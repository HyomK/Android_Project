package com.likefirst.btos.ui.splash

import android.content.Intent
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.Toast
import com.likefirst.btos.R
import com.likefirst.btos.data.entities.User
import com.likefirst.btos.data.entities.UserSign
import com.likefirst.btos.data.local.UserDatabase
import com.likefirst.btos.data.remote.response.Login
import com.likefirst.btos.data.remote.service.AuthService
import com.likefirst.btos.data.remote.view.GetProfileView
import com.likefirst.btos.data.remote.view.SignUpView
import com.likefirst.btos.databinding.ActivityOnboardingBinding
import com.likefirst.btos.ui.BaseActivity

class OnboardingActivity :BaseActivity<ActivityOnboardingBinding> ( ActivityOnboardingBinding::inflate), SignUpView, GetProfileView{

    val authService = AuthService()

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
            val intent = getIntent()
            val bundle = intent.getBundleExtra("mypackage")
            val email = bundle?.getString("email").toString()

            val nickname = binding.onboardingNameEt.text.toString()
            val birth = binding.onboardingAgelist.text.toString().toInt()
            Log.e("SIGNUP", "email:$email\nnickname:$nickname\nbirth:$birth")

            authService.setSignUpView(this)
            authService.signUp(UserSign(email,nickname,birth))
        }
    }

    override fun onSignUpLoading() {
        binding.onboardingLoadingPb.visibility = View.VISIBLE
    }

    override fun onSignUpSuccess(login: Login) {
        binding.onboardingLoadingPb.visibility = View.GONE

        //프로필 정보 가져와서 userdb에 저장
        authService.setGetProfileView(this)
        authService.getProfile(login.userIdx)

        Toast.makeText(this,"회원가입에 성공하였습니다.\n로그인화면으로 돌아갑니다.", Toast.LENGTH_SHORT).show()
        val intent = Intent(this, LoginActivity::class.java)
        finish()
        startActivity(intent)
    }

    override fun onSignUpFailure(code: Int, message: String) {
        binding.onboardingLoadingPb.visibility = View.GONE
        Log.e("SIGNUP/FAIL", message)
        when(code){
            4000 -> {
                Log.e("SIGNUP/FAIL", message)
            }
        }
    }

    override fun onGetProfileViewLoading() {
    }

    override fun onGetProfileViewSuccess(user: User) {
        //UserDB에 프로필 정보 저장
        val userDB = UserDatabase.getInstance(this)?.userDao()
        if(userDB == null){
            userDB?.insert(user)
        } else {
            userDB.deleteAll()
            userDB.insert(user)
        }
        Log.e("PROFILE/API",userDB?.getUser().toString())
    }

    override fun onGetProfileViewFailure(code: Int, message: String) {

    }
}