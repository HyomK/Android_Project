package com.likefirst.btos.ui.splash

import android.content.Intent
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.Toast
import com.likefirst.btos.R
import com.likefirst.btos.data.entities.Plant
import com.likefirst.btos.data.entities.User
import com.likefirst.btos.data.entities.UserSign
import com.likefirst.btos.data.local.PlantDatabase
import com.likefirst.btos.data.local.UserDatabase
import com.likefirst.btos.data.remote.plant.service.PlantService
import com.likefirst.btos.data.remote.users.service.AuthService
import com.likefirst.btos.data.remote.users.view.GetProfileView
import com.likefirst.btos.data.remote.users.view.LoginView
import com.likefirst.btos.data.remote.users.view.SignUpView
import com.likefirst.btos.data.remote.plant.view.PlantListView
import com.likefirst.btos.data.remote.users.response.Login
import com.likefirst.btos.databinding.ActivityOnboardingBinding
import com.likefirst.btos.ui.BaseActivity
import com.likefirst.btos.utils.getJwt
import com.likefirst.btos.utils.saveJwt

class OnboardingActivity :BaseActivity<ActivityOnboardingBinding> ( ActivityOnboardingBinding::inflate),
    SignUpView, GetProfileView, LoginView, PlantListView {

    val authService = AuthService()
    val plantService= PlantService()
    lateinit var email: String



    override fun initAfterBinding() {

        val imm: InputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        val agelist = resources.getStringArray(R.array.onboarding_agelist)
        val arrayAdapter = ArrayAdapter(this, R.layout.onboarding_dropdown_item, agelist)
        binding.onboardingAgelist.setAdapter(arrayAdapter)
        binding.onboardingAgelist.setDropDownBackgroundDrawable(resources.getDrawable(R.drawable.onboarding_age_box))
        binding.onboardingAgelist.dropDownHeight = 400

        //나이 선택 시 키보드 내리기
        binding.onboardingAgeTil.setOnClickListener {
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

        // TODO: Firebase 로그인
        // TODO: initPlant 확인 필요

        Log.e("PLANT_INIT/DONE","DONE")
        authService.setLoginView(this)
        authService.login(email)


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
        if(userDB?.getUser() == null){
            userDB?.insert(user)
        } else {
            userDB.deleteAll()
            userDB.insert(user)
        }
        Log.e("PROFILE/API", userDB?.getUser().toString())
        updatePlantDB()
        gotoFirebaseSignUp(user)
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

  val intent = Intent(this, TutorialActivity::class.java)


      finish()
       startActivity(intent)
    }

    fun gotoFirebaseSignUp(user: User){

        val intent = Intent(this, FirebaseActivity::class.java)
        intent.putExtra("movePos","tutorial")
        intent.putExtra("email",user.email)
        startActivity(intent)
    }


    override fun onLoginFailure(code: Int, message: String) {
        binding.onboardingLoadingPb.visibility = View.GONE

        when(code){
            4000 -> {
                Log.e("LOGIN/FAIL", message)
            }
        }
    }


    fun updatePlantDB(){
        val userDB= UserDatabase.getInstance(this)!!
        val userIdx=userDB.userDao().getUser().userIdx!!
        val plantService = PlantService()
        plantService.setPlantListView(this)
        plantService.loadPlantList( userIdx.toString())

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
        when(code){
            4000-> Log.e( code.toString(),"데이터베이스 연결에 실패하였습니다.")

        }
    }

}