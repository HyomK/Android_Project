package com.likefirst.btos.ui.splash

import android.R.attr
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.app
import com.google.firebase.messaging.FirebaseMessaging
import com.likefirst.btos.ApplicationClass
import com.likefirst.btos.R
import com.likefirst.btos.data.entities.firebase.MessageDTO
import com.likefirst.btos.data.entities.firebase.UserDTO
import com.likefirst.btos.databinding.ActivityLoginBinding
import com.likefirst.btos.ui.BaseActivity
import com.likefirst.btos.ui.main.MainActivity
import android.R.attr.data
import com.google.android.gms.auth.api.signin.*
import com.likefirst.btos.utils.getGSO
import android.R.attr.data
import android.os.Handler
import android.os.Looper
import android.util.Base64
import android.view.View
import android.view.animation.AnimationUtils

import com.google.android.gms.auth.api.signin.GoogleSignInResult
import com.likefirst.btos.data.local.FCMDatabase
import javax.crypto.Cipher
import javax.crypto.Cipher.SECRET_KEY
import javax.crypto.spec.SecretKeySpec


class FirebaseActivity : BaseActivity<ActivityLoginBinding>(ActivityLoginBinding::inflate) {

    companion object {
        const val SECRET_KEY = "BTOS12345678BTOS"
      }



    val RC_SIGN_IN =1111
    val GOOGLE_SIGN_IN =2222
    val fireStore = Firebase.firestore
    lateinit var mAuth: FirebaseAuth
    lateinit var email : String
    private var mAuthListener: FirebaseAuth.AuthStateListener? = null
    lateinit var mGoogleApiClient: GoogleApiClient
    lateinit var googleSignInClient: GoogleSignInClient

    private var mFirebaseDatabase: FirebaseDatabase? = null
    private var mDatabaseReference: DatabaseReference? = null
    private var mChildEventListener: ChildEventListener? = null
    private var userName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAuth = FirebaseAuth.getInstance() //추가
        initFirebaseDatabase();
        initFirebaseAuth();
        initValues();
        signIn()

    }

    private fun initFirebaseAuth() {
        Log.e("Firebase","#########Auth init##############")
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

    private fun initFirebaseDatabase() {
        mFirebaseDatabase = FirebaseDatabase.getInstance()
        mDatabaseReference = mFirebaseDatabase?.getReference("message")
        mChildEventListener = object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {
                val chatData: MessageDTO? = dataSnapshot.getValue(MessageDTO::class.java)
                chatData?.fromToken = dataSnapshot.key

            }
            override fun onChildChanged(dataSnapshot: DataSnapshot, s: String?) {}
            override fun onChildRemoved(dataSnapshot: DataSnapshot) {
            }
            override fun onChildMoved(dataSnapshot: DataSnapshot, s: String?) {}
            override fun onCancelled(databaseError: DatabaseError) {}
        }


        mDatabaseReference?.addChildEventListener( mChildEventListener!!)
    }


    private fun initValues() {
        val user = mAuth!!.currentUser
        if (user == null) {
            userName = "Guest"
        } else {
            userName = user.displayName
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.e("Firebase","#########onActivityResult##############")
        if(requestCode ==RC_SIGN_IN){
            val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data!!)!!
            Log.e("Firebase","#########onActivityResult RC_SIGN IN : "+result?.toString())
            if( result.isSuccess) {
                email =result.signInAccount?.email!!
                firebaseAuthWithGoogle(result.signInAccount)
                updateProfile()
            }
            else{
                updateProfile()
                Toast.makeText(this,"로그인 실패",Toast.LENGTH_SHORT).show()
            }
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
                    Toast.makeText(this,"파이어베이스 토큰 생성 성공", Toast.LENGTH_SHORT).show()
                    moveMainPage(task.result?.user)
                }else{
                    // 틀렸을 때
                        Log.e("Firebase",task.exception?.message.toString())
                    Toast.makeText(this,task.exception?.message, Toast.LENGTH_LONG).show()
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
                //Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
                userData.email = email.substring(0, email.indexOf('@'))
                userData.fcmToken= token

                val fcmDatabase =FCMDatabase.getInstance(this)!!

                if(fcmDatabase.fcmDao().getData() ==null){
                    fcmDatabase.fcmDao().insert(userData)
                }else{
                    fcmDatabase.fcmDao().update(userData)
                }

                val mFireDatabase =  FirebaseDatabase.getInstance(Firebase.app)

                mFireDatabase.getReference("users")
                    .child(userData.email.toString())
                    .setValue(userData)

                /*"users" : {
                    "fatherhana" : {
                      "fcmToken" : "ceWUhK_fO0Q:APA91bG2fqt1DNtT1_9ldvXxZUhsUSsFtyG7usvkybVF-NFiSapSV4VxRgPDnbaf59EIyXv7DGHvav-dcStMEa61NHxIHl_4GKdk0tgrT3rbZeoUXxxGoHwhxUCXcHoKqFExjg8p7ytE",
                     "userEmailID" : "fatherhana"
                   }
                  }
                 구조*/
            })

        }
    }

    private fun signIn() {
        Log.e("firebase","###########SignIn############")
        val signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient)
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }




    override fun initAfterBinding() {

    }

    fun moveMainPage(user: FirebaseUser?){
        if( user!= null){
            when(intent.extras?.getString("movePos")){
                "tutorial" ->{
                    startActivity(Intent(this, TutorialActivity::class.java))
                    finish()
                }
                "main"-> {
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
            }
        }
    }

    // 로그아웃하지 않을 시 자동 로그인
    public override fun onStart() {
        super.onStart()
        moveMainPage(mAuth?.currentUser)
    }


    override fun onPause() {
        super.onPause()
        mGoogleApiClient.stopAutoManage(this);
        mGoogleApiClient.disconnect();
    }
}