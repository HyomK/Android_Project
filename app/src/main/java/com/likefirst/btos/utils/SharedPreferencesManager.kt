package com.likefirst.btos.utils

import android.content.Context
import com.likefirst.btos.ApplicationClass

fun saveJwt(context: Context, jwtToken: String) {
    val editor = context.getSharedPreferences(ApplicationClass.TAG, Context.MODE_PRIVATE).edit()
    editor.putString("jwt", jwtToken)
    editor.apply()
}

fun getJwt(context : Context): String? = context.getSharedPreferences(ApplicationClass.TAG, Context.MODE_PRIVATE).getString("jwt", null)
