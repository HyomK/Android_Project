package com.likefirst.btos.data.remote.notify.service

import org.json.JSONObject

import android.os.AsyncTask
import android.util.Log
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import java.lang.Exception


class FcmSendService (regToken: String, title: String, messsage: String) :AsyncTask<Void, Void, Void>() {
    val message = messsage
    val regToken =regToken
    val title= title
    private val FCM_MESSAGE_URL = "https://fcm.googleapis.com/fcm/send"
    private val SERVER_KEY = "AAAA4ageO_g:APA91bGMNBpRuyibDVwa9RptTRXZb5wFmMXk1Z9VE_tVyb0zl5re63CGQUgud"
    val JSON = "application/json; charset=utf-8".toMediaTypeOrNull()

    override fun doInBackground(vararg p0: Void?): Void? {
                try {
                    val client = OkHttpClient()
                    val json = JSONObject()
                    val dataJson = JSONObject()
                    dataJson.put("body", message)
                    dataJson.put("title", title)
                    json.put("notification", dataJson)
                    json.put("to", regToken)
                    val body: RequestBody = RequestBody.create(JSON, json.toString())
                    val request = Request.Builder()
                        .header("Authorization", "key=" +  SERVER_KEY )
                        .url("https://fcm.googleapis.com/fcm/send")
                        .post(body)
                        .build()
                    val response: Response = client.newCall(request).execute()!!
                    val finalResponse = response.body.toString()
                    Log.e("firebase", "Success sending")
                } catch (e: Exception) {
                    Log.e("firebase", e.toString() + "")
                }
                return null
            }

}