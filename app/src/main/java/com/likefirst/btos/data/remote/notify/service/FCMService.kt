package com.likefirst.btos.data.remote.notify.service

import android.os.Build
import android.provider.Settings.Global.getString
import android.util.Log
import com.google.firebase.database.*
import com.likefirst.btos.data.entities.firebase.UserDTO
import org.json.JSONObject
import java.io.BufferedInputStream
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class FCMService {
   // private val CHANNEL_ID="LinkedFirst_BTOS"
   private val CHANNEL_ID="LikeFirst_BTOS"
    private val FCM_MESSAGE_URL = "https://fcm.googleapis.com/fcm/send"
    private val SERVER_KEY = "AAAA4ageO_g:APA91bGMNBpRuyibDVwa9RptTRXZb5wFmMXk1Z9VE_tVyb0zl5re63CGQUgudTuXBaJnXTQzP__2m-YwSQ7Mefca20Fo_OWaRK23NMGKUQhGtwtP3kblibvqHapWqlYaptwhYXIqqhBw"
    //TODO SEVER KEY  숨기는 법 찾기

    fun sendPostToFCM(destination: String, chatData :UserDTO, message :String) {
        var mFirebaseDatabase = FirebaseDatabase.getInstance()!!
        var mDatabaseReference = mFirebaseDatabase?.getReference("message")!!

        mFirebaseDatabase.getReference("users")
            .child(chatData.email)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    var userData = dataSnapshot.getValue(UserDTO::class.java)
                    val toTimeStamp = Date()
                    val datef =  SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS", Locale.getDefault())
                    val createAt= datef.format(toTimeStamp)
                    Log.e("firebase create: ", createAt)

                    Thread(
                        Runnable {
                            kotlin.run {
                                try {
                                    val root = JSONObject()
                                    val notification = JSONObject()

                                    notification.put("title", "BTOS")
                                    notification.put("body", message)
                                    notification.put("emailID", userData?.email)
                                    notification.put("type","letter")
                                    notification.put("timestamp", createAt)
                                    notification.put("fromToken", userData?.fcmToken)
                                    notification.put("fromUser", userData?.email)
                                    //TODO USER 이름으로 수정

                                    Log.e("Firebase"," from : ${userData?.fcmToken.toString()}")
                                    root.put("data", notification);
                                    root.put("channel_id",CHANNEL_ID)
                                    root.put("to", destination);
                                    // FMC 메시지 생성 end

                                    val Url = URL(FCM_MESSAGE_URL)!!
                                    val conn = Url.openConnection() as HttpURLConnection
                                    conn.setRequestMethod("POST")
                                    conn.setDoOutput(true)
                                    conn.setDoInput(true)
                                    conn.addRequestProperty("Authorization", "key=" + SERVER_KEY)
                                    conn.setRequestProperty("Accept", "application/json")
                                    conn.setRequestProperty("Content-type", "application/json")

                                    val os = conn.getOutputStream()
                                    os.write(root.toString().toByteArray(Charsets.UTF_8));

                                    os.flush();
                                    conn.responseCode
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }

                        }
                    ).start()

                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })

        }
    }

