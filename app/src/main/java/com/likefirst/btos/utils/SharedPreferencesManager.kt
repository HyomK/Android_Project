package com.likefirst.btos.utils

import android.content.Context
import com.likefirst.btos.ApplicationClass
import com.likefirst.btos.ApplicationClass.Companion.mSharedPreferences

fun saveJwt(jwtToken: String) {
    val editor = mSharedPreferences.edit()
    editor.putString("jwt", jwtToken)
    editor.apply()
}

fun getJwt(): String? = mSharedPreferences.getString("jwt", null)
