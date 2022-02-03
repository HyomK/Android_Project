package com.likefirst.btos.ui.splash

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.likefirst.btos.R
import com.likefirst.btos.data.entities.Plant
import com.likefirst.btos.data.entities.User
import com.likefirst.btos.data.entities.UserSign
import com.likefirst.btos.data.local.PlantDatabase
import com.likefirst.btos.data.local.UserDatabase
import com.likefirst.btos.data.remote.response.Login
import com.likefirst.btos.data.remote.service.AuthService
import com.likefirst.btos.data.remote.service.PlantService
import com.likefirst.btos.data.remote.view.GetProfileView
import com.likefirst.btos.data.remote.view.LoginView
import com.likefirst.btos.data.remote.view.SignUpView
import com.likefirst.btos.data.remote.view.plant.PlantInitView
import com.likefirst.btos.data.remote.view.plant.PlantListView
import com.likefirst.btos.databinding.ActivityOnboardingBinding
import com.likefirst.btos.ui.BaseActivity
import com.likefirst.btos.utils.getJwt
import com.likefirst.btos.utils.saveJwt

class OnboardingActivity :BaseActivity<ActivityOnboardingBinding> ( ActivityOnboardingBinding::inflate), SignUpView, GetProfileView, LoginView,PlantInitView, PlantListView {

    val authService = AuthService()
    val plantService= PlantService()
    lateinit var email: String
    private var auth : FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
    }

    override fun initAfterBinding() {

        val imm: InputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        val agelist = resources.getStringArray(R.array.onboarding_agelist)
        val arrayAdapter = ArrayAdapter(this, R.layout.onboarding_dropdown_item, agelist)
        binding.onboardingAgelist.setAdapter(arrayAdapter)
        binding.onboardingAgelist.setDropDownBackgroundDrawable(resources.getDrawable(R.drawable.onboarding_age_box))
        binding.onboardingAgelist.dropDownHeight = 400

        //나이 선택 시 키보드 내리기
        binding.onboardingAgelist.setOnClickListener {
            imm.hideSoftInputFromWindow(binding.onboardingNameEt.getWindowToken(), 0);
        }

        binding.onboardingOkayTv.setOnClickListener {
            // loginactivity에서 넘어온 email 받기
            val intent = getIntent()
            val bundle = intent.getBundleExtra("mypackage")
            email = bundle?.getString("email").toString()

            val nickname = binding.onboardingNameEt.text.toString()
            val birth = binding.onboardingAgelist.text.toString().toInt()
            Log.e("SIGNUP", "email:$email\nnickname:$nickname\nbirth:$birth")

            authService.setSignUpView(this)
            authService.signUp(UserSign(email, nickname, birth))
        }
    }

    override fun onSignUpLoading() {
        binding.onboardingLoadingPb.visibility = View.VISIBLE
    }

    override fun onSignUpSuccess(login: Login) {
        binding.onboardingLoadingPb.visibility = View.GONE
        Toast.makeText(this, "회원가입에 성공하였습니다.", Toast.LENGTH_SHORT).show()
        createAccount(email,"btos1234")
        authService.setLoginView(this)
        authService.login(email)
        plantService.initPlant(login.userIdx)
    }

    override fun onSignUpFailure(code: Int, message: String) {
        binding.onboardingLoadingPb.visibility = View.GONE
        Log.e("SIGNUP/FAIL", message)
        when (code) {
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
        if (userDB == null) {
            userDB?.insert(user)
        } else {
            userDB.deleteAll()
            userDB.insert(user)
        }
        Log.e("PROFILE/API", userDB?.getUser().toString())
        updatePlantDB()

    }

    override fun onGetProfileViewFailure(code: Int, message: String) {

    }

    override fun onLoginLoading() {
        binding.onboardingLoadingPb.visibility = View.VISIBLE
    }

    override fun onLoginSuccess(login: Login) {
        binding.onboardingLoadingPb.visibility = View.GONE

        saveJwt(login.jwt!!)
        Log.e("LOGIN/JWT", getJwt()!!)

        //프로필 정보 가져와서 userdb에 저장
        authService.setGetProfileView(this)
        authService.getProfile(login.userIdx)

        val intent = Intent(this, OnboardingActivity::class.java)
        finish()
        startActivity(intent)
    }

    override fun onLoginFailure(code: Int, message: String) {
        binding.onboardingLoadingPb.visibility = View.GONE

        when(code){
            4000 -> {
                Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
                Log.e("LOGIN/FAIL", message)
            }
        }
    }


    private fun createAccount(email: String, password: String) {
        if (email.isNotEmpty() && password.isNotEmpty()) {
            auth?.createUserWithEmailAndPassword(email, password)
                ?.addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(
                            this, "계정 생성 완료.",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            this, "계정 생성 실패",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
    }



    override fun onPlantInitSuccess() {
    }

    override fun onPlantInitFailure(code: Int, message: String) {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
        Log.e("PlantInit/FAIL",code.toString()+ message)
    }

    fun updatePlantDB(){
        val userDB= UserDatabase.getInstance(this)!!
        val USERIDX=userDB.userDao().getUser().userIdx!!
        val plantService =PlantService()
        plantService.setPlantListView(this)
        plantService.loadPlantList( USERIDX.toString())

    }

    override fun onPlantListLoading() {

    }

    override fun onPlantListSuccess(plantList: ArrayList<Plant>) {
        val plantDB = PlantDatabase.getInstance(this)
        Log.d("Plant/API",plantList.toString())
        plantList.forEach { i ->
            run {
                if (plantDB?.plantDao()?.getPlant(i.plantIdx) == null) {
                    plantDB?.plantDao()?.insert(i)
                } else {
                    plantDB?.plantDao()?.update(i)
                }
            }
        }  // 전체 화분 목록 DB 업데이트
    }



    override fun onPlantListFailure(code: Int, message: String) {
        Log.d("Plant/API",code.toString()+"fail to load...")
    }

}