package com.likefirst.btos.utils

import android.content.Context
import com.likefirst.btos.ApplicationClass.Companion.mSharedPreferences

fun saveJwt(context: Context, jwtToken: String) {
    val editor = mSharedPreferences.edit()
    editor.putString("jwt", jwtToken)
    editor.apply()
}

fun getJwt(context : Context): String? = mSharedPreferences.getString("jwt", null)
