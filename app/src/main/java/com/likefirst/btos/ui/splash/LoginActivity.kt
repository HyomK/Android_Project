package com.likefirst.btos.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.internal.OnConnectionFailedListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.likefirst.btos.R
import com.likefirst.btos.data.entities.Plant
import com.likefirst.btos.data.entities.User
import com.likefirst.btos.data.local.PlantDatabase
import com.likefirst.btos.data.local.UserDatabase
import com.likefirst.btos.data.remote.plant.service.PlantService
import com.likefirst.btos.data.remote.users.response.Login
import com.likefirst.btos.data.remote.users.service.AuthService
import com.likefirst.btos.data.remote.users.view.AutoLoginView
import com.likefirst.btos.data.remote.users.view.GetProfileView
import com.likefirst.btos.data.remote.users.view.LoginView
import com.likefirst.btos.data.remote.plant.view.PlantInitView
import com.likefirst.btos.data.remote.plant.view.PlantListView
import com.likefirst.btos.databinding.ActivityLoginBinding
import com.likefirst.btos.ui.BaseActivity
import com.likefirst.btos.ui.main.MainActivity
import com.likefirst.btos.utils.getGSO
import com.likefirst.btos.utils.getJwt
import com.likefirst.btos.utils.saveJwt
import com.likefirst.btos.utils.saveUserIdx

class LoginActivity
    : BaseActivity<ActivityLoginBinding>(ActivityLoginBinding::inflate), OnConnectionFailedListener,
    LoginView, AutoLoginView, GetProfileView,
    PlantListView, PlantInitView {

    val G_SIGN_IN : Int = 1
    private var GOOGLE_LOGIN_CODE = 9001
    lateinit var googleSignInClient: GoogleSignInClient
    lateinit var email : String
    private var auth : FirebaseAuth? = null
    val authService = AuthService()
    val plantService= PlantService()

    val fireStore = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
    }

    override fun initAfterBinding() {

        val animFadeOut = AnimationUtils.loadAnimation(applicationContext, R.anim.fade_out)
        val animFadeIn = AnimationUtils.loadAnimation(applicationContext, R.anim.fade_in)

        // animation_logo_FadeOut
        Handler(Looper.getMainLooper()).postDelayed({
            binding.loginLogoIv.visibility = View.VISIBLE

            //자동로그인
            authService.setAutoLoginView(this)
            Log.e("AUTOLOGIN/JWT",getJwt().toString())
            if(getJwt()!=null)
                authService.autologin()
            else{
                binding.loginLogoIv.startAnimation(animFadeOut)
                // animation_loginText_FadeIn
                binding.loginWelcomeTv.visibility = View.VISIBLE
                binding.loginWelcomeTv.startAnimation(animFadeIn)
                binding.loginGoogleLoginTv.visibility = View.VISIBLE
                binding.loginGoogleLoginTv.startAnimation(animFadeIn)
            }

        },3000)

        val gso = getGSO()


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
           // firebaseAuthWithGoogle(account)
            email = account?.email.toString()
            Log.e("account", email)

            authService.setLoginView(this)
            authService.login(email)
        }
    }

    override fun onLoginLoading() {
        binding.loginLoadingPb.visibility = View.VISIBLE
    }

    override fun onLoginSuccess(login: Login) {
        binding.loginLoadingPb.visibility = View.GONE

        saveJwt(login.jwt!!)
        Log.e("LOGIN/JWT", getJwt()!!)

        //프로필 정보 가져와서 userdb에 저장
        authService.setGetProfileView(this)
        authService.getProfile(login.userIdx)
        saveUserIdx(login.userIdx)
        signIn(email,"btos1234")
        Log.d("UserDB", login.toString())
        // TODO: Firebase 로그인
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
                val bundle = Bundle()
                bundle.putString("email",email)
                intent.putExtra("mypackage",bundle)
                finish()
                startActivity(intent)
            }
        }
    }

    override fun onAutoLoginLoading() {
        binding.loginLoadingPb.visibility = View.VISIBLE
    }

    override fun onAutoLoginSuccess(login : Login) {
        binding.loginLoadingPb.visibility = View.GONE

        //프로필 정보 가져와서 userdb에 저장
        authService.setGetProfileView(this)
        authService.getProfile(login.userIdx)
        saveUserIdx(login.userIdx)

        val intent = Intent(this, MainActivity::class.java)
        finish()
        startActivity(intent)
    }

    override fun onAutoLoginFailure(code: Int, message: String) {
        binding.loginLoadingPb.visibility = View.GONE
    }

    override fun onGetProfileViewLoading() {
        binding.loginLoadingPb.visibility = View.VISIBLE

    }

    override fun onGetProfileViewSuccess(user: User) {
        binding.loginLoadingPb.visibility = View.GONE

        //UserDB에 프로필 정보 저장
        val userDB = UserDatabase.getInstance(this)?.userDao()
        if(userDB?.getUser() == null){
            userDB?.insert(user)
        } else {
            userDB.update(user)
        }

        updatePlantDB()
    }

    override fun onGetProfileViewFailure(code: Int, message: String) {
    }

    private fun firebaseAuthWithGoogle(account : GoogleSignInAccount?){
        var credential = GoogleAuthProvider.getCredential(account?.idToken,null)
        auth?.signInWithCredential(credential)
            ?.addOnCompleteListener{
                    task ->
                if(task.isSuccessful){
                    // 아이디, 비밀번호 맞을 때
                    Toast.makeText(this,"FIREBASE LOGIN SUCCESS",Toast.LENGTH_LONG).show()
                }else{
                    // 틀렸을 때
                    Toast.makeText(this,task.exception?.message,Toast.LENGTH_SHORT).show()
                }
            }
    }


//    fun saveToken(){
//        //프로필 불러오기
//
//        fireStore.collection("users").document(uid)
//            .addSnapshotListener { documentSnapshot, _ ->
//                if (documentSnapshot == null) return@addSnapshotListener
//
//                val userDTO = documentSnapshot.toObject(UserDTO::class.java)
//                if (userDTO?.userId != null) {
//
//                    // 토큰이 변경되었을 경우 갱신
//                    if(userDTO.token != token){
//                        Log.d(TAG, "profileLoad: 토큰 변경되었음.")
//                        val newUserDTO = UserDTO(userDTO.uId,userDTO.userId,
//                            userDTO.imageUri,userDTO.score,userDTO.sharing,userDTO.area,token)
//                        fireStore.collection("users").document(uid).set(newUserDTO)
//
//                        // 유저정보 라이브데이터 변경하기
//                        this.userDTO.value = newUserDTO
//                    }
//
//                    // 아니면 그냥 불러옴
//                    else {
//                        Log.d(TAG, "profileLoad: 이미 동일한 토큰이 존재함.")
//                        this.userDTO.value = userDTO!!
//                    }
//                }
//         }
//    }

    // TODO: Firebase 로그인
    private fun signIn(email: String, password: String) {
        if (email.isNotEmpty() && password.isNotEmpty()) {
            auth?.signInWithEmailAndPassword(email, password)
                ?.addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(
                            baseContext, "로그인에 성공 하였습니다.",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        createAccount(email, password)
                        auth?.signInWithEmailAndPassword(email, password)
                            ?.addOnCompleteListener(this) { task ->
                                if (task.isSuccessful) {
                                    Toast.makeText(
                                        baseContext, "로그인에 성공 하였습니다.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } else {
                                    Toast.makeText(
                                        baseContext, "로그인에 실패 하였습니다.",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                }
                            }
                    }
                }
        }
    }

    // TODO: Firebase 로그인
    private fun createAccount(email:     String, password: String) {
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


    fun updatePlantDB(){
        val userDB= UserDatabase.getInstance(this)!!
        val USERIDX=userDB.userDao().getUser().userIdx!!
        plantService.setPlantListView(this)
        plantService.loadPlantList(USERIDX.toString())
    }

    override fun onPlantListLoading() {

    }

    override fun onPlantListSuccess(plantList: ArrayList<Plant>) {
        val plantDB = PlantDatabase.getInstance(this)
        Log.d("PlantList/API",plantList.toString())
        plantList.forEach { i ->
            run {
                if (plantDB?.plantDao()?.getPlant(i.plantIdx) == null) {
                    plantDB?.plantDao()?.insert(i)
                } else {
                    if(i.plantIdx==1 && i.plantStatus=="inactive"){
                        val userDB= UserDatabase.getInstance(this)!!
                        val USERIDX=userDB.userDao().getUser().userIdx!!
                        plantService.setPlantInitView(this)
                        plantService.initPlant(USERIDX.toString())
                    }
                    plantDB?.plantDao()?.update(i)
                }
            }
        }  // 전체 화분 목록 DB 업데이트
    }



    override fun onPlantListFailure(code: Int, message: String) {
        Log.d("Plant/API",code.toString()+"fail to load...")
    }

    override fun onPlantInitSuccess(userId: Int) {
        Log.e("PlantINIT/API","Success")
    }

    override fun onPlantInitFailure(code: Int, message: String) {
        Log.e("PlantINIT/API","FAIL ...")
    }


}