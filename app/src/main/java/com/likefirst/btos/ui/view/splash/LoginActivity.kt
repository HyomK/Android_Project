package com.likefirst.btos.ui.view.splash

import android.app.NotificationManager
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.internal.OnConnectionFailedListener
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.AuthStateListener
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.likefirst.btos.ApplicationClass
import com.likefirst.btos.BuildConfig
import com.likefirst.btos.R
import com.likefirst.btos.data.entities.User
import com.likefirst.btos.data.entities.UserEmail
import com.likefirst.btos.data.local.UserDatabase
import com.likefirst.btos.data.remote.notify.service.FcmTokenService
import com.likefirst.btos.data.remote.notify.view.FcmTokenView
import com.likefirst.btos.ui.viewModel.PlantInfoViewModel
import com.likefirst.btos.data.remote.service.AuthService
import com.likefirst.btos.data.remote.users.response.Login
import com.likefirst.btos.data.remote.users.view.AutoLoginView
import com.likefirst.btos.data.remote.users.view.GetProfileView
import com.likefirst.btos.data.remote.users.view.LoginView
import com.likefirst.btos.ui.BaseActivity
import com.likefirst.btos.ui.view.main.MainActivity
import com.likefirst.btos.utils.*
import com.likefirst.btos.databinding.ActivityLoginBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlin.system.exitProcess

@AndroidEntryPoint
class LoginActivity
    : BaseActivity<ActivityLoginBinding>(ActivityLoginBinding::inflate), OnConnectionFailedListener,
    LoginView, AutoLoginView, GetProfileView, FcmTokenView{

    val G_SIGN_IN : Int = 1
    private var GOOGLE_LOGIN_CODE = 9001
    val RC_SIGN_IN =1111
    lateinit var googleSignInClient: GoogleSignInClient
    lateinit var email : String
    private val handler= Handler(Looper.getMainLooper())
    private var stop = false
    var count = 0

    val authService = AuthService()
    val fcmTokenService = FcmTokenService()

    val fireStore = Firebase.firestore
    lateinit var mAuth: FirebaseAuth
    private var mAuthListener: AuthStateListener? = null
    lateinit var mGoogleApiClient: GoogleApiClient
    private var userName: String? = null

    private val DEFAULT_WEB_CLIENT_KEY = BuildConfig.btos_default_web_client_id

    val  plantViewModel by viewModels<PlantInfoViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val networkConnect = NetworkConnection(this)
        networkConnect.observe(this) { isConnected ->
            run {
                Log.e("network connection",isConnected)
                if (isConnected == "false" || isConnected == "null") {
                    GlobalScope.launch {
                        Snackbar.make(binding.root,
                            "인터넷 연결 후 재접속 해주세요.\n어플리케이션을 종료합니다.",Snackbar.LENGTH_INDEFINITE).show()
                        delay(5000)
                        overridePendingTransition( R.anim.fade_in, R.anim.fade_out);
                        exitProcess(0)
                    }
                }
            }
        }
        mAuth = FirebaseAuth.getInstance()
        initFirebaseAuth()
        initValues()

    }




    override fun initAfterBinding() {
        val animFadeOut = AnimationUtils.loadAnimation(applicationContext, R.anim.fade_out)
        val animFadeIn = AnimationUtils.loadAnimation(applicationContext, R.anim.fade_in)

        // animation_logo_FadeOut
        handler.postDelayed({
            if(!stop) {
                binding.loginLogoIv.visibility = View.VISIBLE

                //자동로그인
                authService.setAutoLoginView(this)
                Log.e("AUTOLOGIN/JWT", getJwt().toString())
                if (getJwt() != null)
                    authService.autologin()
                else {
                    binding.loginLogoIv.startAnimation(animFadeOut)
                    // animation_loginText_FadeIn
                    binding.loginWelcomeTv.visibility = View.VISIBLE
                    binding.loginWelcomeTv.startAnimation(animFadeIn)
                    binding.loginGoogleLoginTv.visibility = View.VISIBLE
                    binding.loginGoogleLoginTv.startAnimation(animFadeIn)
                }
            }

        },3000)


        val gso = getGSO()
        googleSignInClient = GoogleSignIn.getClient(this, gso)
        binding.loginGoogleLoginTv.setOnClickListener{
            var signInIntent : Intent = googleSignInClient.signInIntent
            startActivityForResult(signInIntent, G_SIGN_IN)
        }


    }



    override fun onConnectionFailed(p0: ConnectionResult) {}

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == G_SIGN_IN){
            if(resultCode!=0){
                Log.e("RESULTCODE",resultCode.toString())
                val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
                val account = task.getResult(ApiException::class.java)
                email = account?.email.toString()
                Log.e("account ", email )
                authService.setLoginView(this)
                authService.login(UserEmail(email))
            }
        }else if(requestCode ==RC_SIGN_IN){
            val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data!!)!!
            Log.e("Firebase","#########onActivityResult RC_SIGN IN : "+result?.toString())
            if( result.isSuccess) {
                email =result.signInAccount?.email!!
                firebaseAuthWithGoogle(result.signInAccount)
                updateProfile()
            }else{
                Log.e("Firebase","#########onActivityResult RC_SIGN IN : Fail..... exit")
            }
        }
    }

    override fun onLoginLoading() {
        setLoadingView()
    }

    override fun onLoginSuccess(login: Login) {
        binding.loginLoadingPb.visibility = View.GONE

        saveJwt(login.jwt!!)
        Log.e("LOGIN/JWT", getJwt()!!)

        //프로필 정보 가져와서 userdb에 저장
        authService.setGetProfileView(this)
        authService.getProfile(login.userIdx)

    }

    override fun onLoginFailure(code: Int, message: String) {
        binding.loginLoadingPb.visibility = View.GONE

        when(code){
            4000 -> {
                Log.e("LOGIN/FAIL", message)
            }
            5003 -> {
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
        setLoadingView()
    }

    override fun onAutoLoginSuccess(login : Login) {
        binding.loginLoadingPb.visibility = View.GONE
        //프로필 정보 가져와서 userdb에 저장
        authService.setGetProfileView(this)
        authService.getProfile(login.userIdx)
    }



    override fun onAutoLoginFailure(code: Int, message: String) {
        binding.loginLoadingPb.visibility = View.GONE
    }

    override fun onGetProfileViewLoading() {
        setLoadingView()

    }

    override fun onGetProfileViewSuccess(user: User) {
        binding.loginLoadingPb.visibility = View.GONE

        //UserDB에 프로필 정보 저장

        Log.e("PROFILE/API",user.toString())
        val userDB = UserDatabase.getInstance(this)?.userDao()
        if(userDB?.getUser() == null){
            userDB?.insert(user)
        } else {
            userDB.update(user)
        }
        Log.e("PROFILE/ROOMDB",userDB?.getUser().toString())
        saveUserIdx(user.userIdx!!)
        updatePlantDB()
        moveMainPage(mAuth?.currentUser)
    }

    override fun onGetProfileViewFailure(code: Int, message: String) {
    }

    fun updatePlantDB(){
        val userDB= UserDatabase.getInstance(this)!!
        val USERIDX=userDB.userDao().getUser().userIdx!!
        val result =  plantViewModel.getPlantList()
        result.observe(this, Observer {
            Log.e("HILT_TEST",it.toString())
        })
        plantViewModel.loadPlantItemList(USERIDX)
    }


    private fun initFirebaseAuth() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(DEFAULT_WEB_CLIENT_KEY)
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
                    updateProfile()
                    moveMainPage(task.result?.user)
                }else{
                    // 틀렸을 때
                    Log.e("Firebase",task.exception?.message.toString())
                }
            }
    }


    fun updateProfile(){
        mAuth = FirebaseAuth.getInstance()
        val user =  mAuth?.currentUser
        if(user == null) {

        }else{
            FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener {
                    task-> if(!task.isSuccessful){
                Log.w(ApplicationClass.TAG,"FetchingFCM registration token failed", task.exception)
                return@OnCompleteListener
            }
                val token = task.result
                val msg = getString(R.string.msg_token_fmt, token)
                Log.e("FIREBASE", msg)

                fcmTokenService.setFcmTokenView(this)
                fcmTokenService.postFcmToken(getUserIdx(),token)
            })

        }
    }

    private fun firbaseSignIn() {
        val signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient)
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    fun moveMainPage(user: FirebaseUser?){
        if( user!= null){
            //TODO 이용약관 동의 다이얼로그
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }else{
            Log.e("count", "null ${++count} " )
            //initFirebaseDatabase()
            firbaseSignIn()
        }
    }


    public override fun onStart() {
        super.onStart()
        val notificationManager: NotificationManager =getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancelAll();

    }

    override fun onPause() {
        super.onPause()
        mGoogleApiClient.stopAutoManage(this);
        mGoogleApiClient.disconnect();

    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
        stop=true
    }

    override fun onLoadingFcmToken() {}

    override fun onSuccessFcmToken() {}

    override fun onFailureFcmToken(code : Int, msg: String) {
        Log.e("FCM-API - fail","${code}= ${msg}")
    }

    fun setLoadingView(){
        binding.loginLoadingPb.visibility=View.VISIBLE
        binding.loginLoadingPb.apply {
            setAnimation("sprout_loading.json")
            visibility = View.VISIBLE
            playAnimation()
        }
    }

}