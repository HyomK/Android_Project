package com.likefirst.btos.ui.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.app
import com.google.firebase.messaging.FirebaseMessaging
import com.likefirst.btos.ApplicationClass
import com.likefirst.btos.R
import com.likefirst.btos.data.entities.Plant
import com.likefirst.btos.data.entities.User
import com.likefirst.btos.data.entities.UserEmail
import com.likefirst.btos.data.entities.UserSign
import com.likefirst.btos.data.entities.firebase.UserDTO
import com.likefirst.btos.data.local.FCMDatabase
import com.likefirst.btos.data.local.PlantDatabase
import com.likefirst.btos.data.local.UserDatabase
import com.likefirst.btos.data.remote.plant.service.PlantService
import com.likefirst.btos.data.remote.plant.view.PlantListView
import com.likefirst.btos.data.remote.service.AuthService
import com.likefirst.btos.data.remote.users.response.Login
import com.likefirst.btos.data.remote.users.view.GetProfileView
import com.likefirst.btos.data.remote.users.view.LoginView
import com.likefirst.btos.data.remote.users.view.SignUpView
import com.likefirst.btos.databinding.ActivityOnboardingBinding
import com.likefirst.btos.ui.BaseActivity
import com.likefirst.btos.ui.main.MainActivity
import com.likefirst.btos.utils.getGSO
import com.likefirst.btos.utils.getJwt
import com.likefirst.btos.utils.saveJwt
import com.likefirst.btos.utils.saveUserIdx
import java.util.regex.Pattern

class OnboardingActivity :BaseActivity<ActivityOnboardingBinding> ( ActivityOnboardingBinding::inflate),
    SignUpView, GetProfileView, LoginView, PlantListView {

    val authService = AuthService()
    val plantService= PlantService()
    lateinit var email: String
    private var auth : FirebaseAuth? = null

    val RC_SIGN_IN =1111
    val fireStore = Firebase.firestore
    lateinit var mAuth: FirebaseAuth
    private var mAuthListener: FirebaseAuth.AuthStateListener? = null
    lateinit var mGoogleApiClient: GoogleApiClient

    private var userName: String? = null
    private var nickname: String? = null

    private var mFirebaseDatabase: FirebaseDatabase? = null
    private var mDatabaseReference: DatabaseReference? = null
    private var mChildEventListener: ChildEventListener? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val dialog = LoginDialogFragment()
        dialog.show(supportFragmentManager, "")
        mAuth = FirebaseAuth.getInstance()
        initFirebaseDatabase()
        initFirebaseAuth()

    }

    @SuppressLint("SetTextI18n")
    override fun initAfterBinding() {
        val agelist = resources.getStringArray(R.array.onboarding_agelist)
        val arrayAdapter = ArrayAdapter(this, R.layout.onboarding_dropdown_item, agelist)
        binding.onboardingAgelist.setAdapter(arrayAdapter)
        binding.onboardingAgelist.setDropDownBackgroundDrawable(resources.getDrawable(R.drawable.onboarding_age_box))
        binding.onboardingAgelist.dropDownHeight = 800

        //나이 선택 시 키보드 내리기
        binding.onboardingAgeTil.setOnClickListener {
            hideKeyboard(it)
        }
        binding.onboardingAgelist.setOnClickListener {
            hideKeyboard(it)
        }

        binding.onboardingNotageCb.setOnClickListener {
            if(binding.onboardingNotageCb.isChecked){
                binding.ageError.visibility = View.VISIBLE
                binding.ageError.text = "생년 정보가 들어가지 않습니다."
            }
            else{
                binding.ageError.visibility = View.GONE
            }
        }

        binding.onboardingOkayTv.setOnClickListener {

            var checkvali = false
            var birth : Int? = 0

            // loginactivity에서 넘어온 email 받기
            val intent = getIntent()
            val bundle = intent.getBundleExtra("mypackage")
            email = bundle?.getString("email").toString()
            nickname = binding.onboardingNameEt.text.toString()
            var birth_string = binding.onboardingAgelist.text.toString()


            //닉네임 : 빈칸, 한글, 비속어 금지
            // 생일 선택 여부

            if(nickname == ""){
                binding.ageError.visibility = View.GONE
                binding.nicknameError.visibility = View.VISIBLE
                binding.nicknameError.text = "닉네임을 입력해주세요."
            }
            else if(!Pattern.matches("^[가-힣]*\$",nickname)){
                binding.ageError.visibility = View.GONE
                binding.nicknameError.visibility = View.VISIBLE
                binding.nicknameError.text = "한글만 가능합니다."
            }
            else if(birth_string == "선택안함" && !binding.onboardingNotageCb.isChecked) {
                binding.nicknameError.visibility = View.GONE
                binding.ageError.visibility = View.VISIBLE
                binding.ageError.text = "나이를 선택해주세요.\n공개하고 싶지 않다면 아래를 클릭하세요!"
            }
            else {
                binding.nicknameError.visibility = View.GONE
                binding.ageError.visibility = View.GONE
                checkvali = true
                if(binding.onboardingNotageCb.isChecked) birth = null
                else birth == binding.onboardingAgelist.text.toString().toInt()
            }

            if(checkvali){
                Log.e("SIGNUP", "email:$email\nnickname:$nickname\nbirth:$birth")
                authService.setSignUpView(this)
                authService.signUp(UserSign(email, nickname!!, birth))
            }
        }
    }

    fun goToTutorial(){
        val intent = Intent(this, TutorialActivity::class.java)
        intent.putExtra("nickname",nickname)
        startActivity(intent)
        finish()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode ==RC_SIGN_IN){
            val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data!!)!!
            Log.e("Firebase","#########onActivityResult RC_SIGN IN : "+result?.toString())
            if( result.isSuccess) {
                email =result.signInAccount?.email!!
                firebaseAuthWithGoogle(result.signInAccount)
                updateProfile()
            }
        }
    }

    override fun onSignUpLoading() {
        binding.onboardingLoadingPb.visibility = View.VISIBLE
    }

    override fun onSignUpSuccess(login: Login) {
        binding.onboardingLoadingPb.visibility = View.GONE
        Toast.makeText(this, "회원가입에 성공하였습니다.", Toast.LENGTH_SHORT).show()
        Log.e("PLANT_INIT/DONE","DONE")
        authService.setLoginView(this)
        authService.login(UserEmail(email))
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
        if(userDB?.getUser() == null){
            userDB?.insert(user)
        } else {
            userDB.deleteAll()
            userDB.insert(user)
        }
        Log.e("PROFILE/API", userDB?.getUser().toString())
        saveUserIdx(user.userIdx!!)
        updatePlantDB()
        firbaseSignIn()
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
        val USERIDX=userDB.userDao().getUser().userIdx!!
        val plantService = PlantService()
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
    private fun initFirebaseAuth() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.btos_default_web_client_id))
            .requestEmail()
            .build()
        mGoogleApiClient = GoogleApiClient.Builder(this)
            .enableAutoManage(this){}
            .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
            .build()
        mAuthListener = FirebaseAuth.AuthStateListener() { updateProfile() }
    }

    private fun initValues() {
        val user = mAuth!!.currentUser
        if (user == null) {
            userName = "Guest"
        } else {
            userName = user.displayName
        }
    }

    fun firebaseAuthWithGoogle(account : GoogleSignInAccount?){
        var credential = GoogleAuthProvider.getCredential(account?.idToken,null)
        mAuth?.signInWithCredential(credential)
            ?.addOnCompleteListener{
                    task ->
                if(task.isSuccessful){
                    // 아이디, 비밀번호 맞을 때
                    Snackbar.make(binding.root,"파이어베이스 토큰 생성 성공",Snackbar.LENGTH_SHORT).show()
                    initValues()
                    updateProfile()
                    goToTutorial() //TODO 위치 수정

                }else{
                    // 틀렸을 때
                    Log.e("Firebase-login fail",task.exception?.message.toString())
                }
            }
    }


    fun updateProfile(){
        mAuth = FirebaseAuth.getInstance()
        val user =  mAuth?.currentUser
        if(user == null) {
            //TODO 비로그인 상태 일때 처리
            Log.e("FIREBASE", "실패! 비로그인 상태입니다")
        }else{
            var userData = UserDTO()
            FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener {
                    task-> if(!task.isSuccessful){
                Log.w(ApplicationClass.TAG,"FetchingFCM registration token failed", task.exception)
                return@OnCompleteListener
            }
                val token = task.result
                val msg = getString(R.string.msg_token_fmt, token)
                Log.e("FIREBASE", msg)
                userData.email = email.substring(0, email.indexOf('@'))
                userData.fcmToken= token
                val fcmDatabase = FCMDatabase.getInstance(this)!!
                if(fcmDatabase.fcmDao().getData() ==null){
                    fcmDatabase.fcmDao().insert(userData)
                }else{
                    fcmDatabase.fcmDao().update(userData)
                }

                val mFireDatabase =  FirebaseDatabase.getInstance(Firebase.app)
                mFireDatabase.getReference("users")
                    .child(userData.email.toString())
                    .setValue(userData)
            })
        }
    }


    private fun firbaseSignIn() {
        val signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient)
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }


    private fun initFirebaseDatabase() {
        mFirebaseDatabase = FirebaseDatabase.getInstance()
        mDatabaseReference = mFirebaseDatabase?.getReference("users")
        mChildEventListener = object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {
                // child 내에 있는 데이터만큼 반복합니다.
            }
            override fun onChildChanged(dataSnapshot: DataSnapshot, s: String?) {}
            override fun onChildRemoved(dataSnapshot: DataSnapshot) {
            }
            override fun onChildMoved(dataSnapshot: DataSnapshot, s: String?) {}
            override fun onCancelled(databaseError: DatabaseError) {}
        }
        mDatabaseReference?.addChildEventListener( mChildEventListener!!)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this,LoginActivity::class.java)
        finish()
        startActivity(intent)
    }

    override fun onPause() {
        super.onPause()
        mGoogleApiClient.stopAutoManage(this);
        mGoogleApiClient.disconnect();
    }

    override fun onDestroy() {
        super.onDestroy()
        val gso = getGSO()
        val googleSignInClient = GoogleSignIn.getClient(this, gso)
        googleSignInClient.signOut()
    }
}