package com.likefirst.btos.utils

import android.content.Context
import android.provider.Settings.Global.getString
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.likefirst.btos.ApplicationClass.Companion.mSharedPreferences
import com.likefirst.btos.R
import com.likefirst.btos.data.local.UserDatabase
import java.util.*

fun saveJwt(jwtToken: String) {
    val editor = mSharedPreferences.edit()
    editor.putString("jwt", jwtToken)
    editor.apply()
}

fun getJwt(): String? = mSharedPreferences.getString("jwt", null)

fun removeJwt(){
    val editor = mSharedPreferences.edit()
    editor.remove("jwt")
    editor.commit()
}

fun getGSO(): GoogleSignInOptions {
    val web_client_id="969188195320-ne8ipt9t7o9lgvp403r2afkn2sfvfc1n.apps.googleusercontent.com"
    return GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestEmail()
        .requestIdToken(web_client_id)
        .build()
}
fun saveLastPostingDate(date : Date){
    val editor = mSharedPreferences.edit()
    editor.putLong("lastPostingDate", date.time)
    editor.apply()
}

fun getLastPostingDate() : Date {
    // TODO: 앱 배포시에는 dateLong 기본값을 오늘날짜로 해주어야함 Date().time
    val dateDefault = GregorianCalendar(1999, 0,6)
    val date = dateDefault.time
//    val dateLong = mSharedPreferences.getLong("lastPostingDate", Date().time)
    val dateLong = mSharedPreferences.getLong("lastPostingDate", date.time)
    return Date(dateLong)
}

fun saveUserIdx(userIdx : Int) {
    val editor = mSharedPreferences.edit()
    editor.putInt("userIdx", userIdx)
    editor.apply()
}

fun getUserIdx() : Int = mSharedPreferences.getInt("userIdx", 0)

fun saveUserName(name : String){
    val editor = mSharedPreferences.edit()
    editor.putString("UserName",name)
    editor.apply()
}

fun getUserName():String?{
    return mSharedPreferences.getString("UserName","undefine")
}

fun saveAlarmSound(isSound : Boolean){
    val editor = mSharedPreferences.edit()
    editor.putBoolean("AlarmState",isSound)
    editor.apply()
}

fun getAlarmSound():Boolean{
    return mSharedPreferences.getBoolean("AlarmState",true)
}

fun deleteUserInfo(){
    val editor = mSharedPreferences.edit()
    editor.remove("UserName")
    editor.remove("AlarmState")
    editor.apply()
}