package com.likefirst.btos.utils

import com.likefirst.btos.ApplicationClass.Companion.mSharedPreferences
import java.util.*

fun saveJwt(jwtToken: String) {
    val editor = mSharedPreferences.edit()
    editor.putString("jwt", jwtToken)
    editor.apply()
}

fun getJwt(): String? = mSharedPreferences.getString("jwt", null)

fun saveLastPostingDate(date : Date){
    val editor = mSharedPreferences.edit()
    editor.putLong("lastPostingDate", date.time)
    editor.apply()
}

fun getLastPostingDate() : Date {
    val dateLong = mSharedPreferences.getLong("lastPostingDate", 0L)
    return Date(dateLong)
}