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
    val dateLong = mSharedPreferences.getLong("lastPostingDate", Date().time)
    return Date(dateLong)
}