package com.likefirst.btos.utils

import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.likefirst.btos.ApplicationClass.Companion.mSharedPreferences
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
    return GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build()
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