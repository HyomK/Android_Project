package com.likefirst.btos.data.remote.notify.service

import android.app.*
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.graphics.drawable.IconCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.likefirst.btos.ui.main.MainActivity
import android.os.Looper
import android.util.Log

import android.widget.Toast
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.likefirst.btos.R
import com.likefirst.btos.utils.fcm.MyWorker
import android.os.PowerManager
import android.media.AudioAttributes
import com.google.common.reflect.TypeToken
import com.google.firebase.database.FirebaseDatabase
import com.google.gson.GsonBuilder
import com.likefirst.btos.data.entities.firebase.MessageDTO
import com.likefirst.btos.data.entities.firebase.UserDTO
import com.likefirst.btos.data.local.FCMDatabase
import com.likefirst.btos.data.remote.notify.view.NoticeAPIView
import org.json.JSONArray
import java.lang.reflect.Type
import android.content.pm.ResolveInfo

import android.content.pm.PackageManager
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.ViewModelProvider
import com.likefirst.btos.data.remote.notify.view.SharedNotifyModel


class MyFirebaseMessagingService : FirebaseMessagingService() {
    val TAG = "Firebase"
    lateinit var listener : NoticeAPIView

    // 메세지가 수신되면 호출
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.i("### msg : ", remoteMessage.toString());

        // 서버에서 직접 보냈을 때
        if(remoteMessage.notification != null){
            sendNotification(remoteMessage.notification?.title,
                remoteMessage.notification?.body!!)
            if (true) {
                scheduleJob();
            } else {
                handleNow();
            }
        }

        // 다른 기기에서 서버로 보냈을 때
        else if(remoteMessage.data.isNotEmpty()){
            val title = remoteMessage.data["title"]!!
            val message = remoteMessage.data["body"]!!
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                sendMessageNotification(remoteMessage.data )
            }
            else{
                sendNotification(remoteMessage.notification?.title, remoteMessage.notification?.body!!)
            }
            if (true) {
                scheduleJob();
            } else {
                handleNow();
            }
        }
    }


    private fun handleNow() {
        Log.d(TAG, "Short lived task is done.")
    }


    private fun scheduleJob() {
        val work = OneTimeWorkRequest.Builder(MyWorker::class.java)
            .build()
        WorkManager.getInstance().beginWith(work).enqueue()
    }

    // Firebase Cloud Messaging Server 가 대기중인 메세지를 삭제 시 호출
    override fun onDeletedMessages() {
        super.onDeletedMessages()
    }

    // 메세지가 서버로 전송 성공 했을때 호출
    override fun onMessageSent(p0: String) {
        super.onMessageSent(p0)
        Log.e("Firebase", "sending success ")
    }

    // 메세지가 서버로 전송 실패 했을때 호출
    override fun onSendError(p0: String, p1: Exception) {
        super.onSendError(p0, p1)
    }

    // 새로운 토큰이 생성 될 때 호출
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d(TAG,"Refreshed token : $token")
        val FirebaseDB =FCMDatabase.getInstance(this)!!.fcmDao()
        val prev = FirebaseDB.getData()
        if(prev!= null )FirebaseDB.update(UserDTO(prev.email, token))

        sendRegistrationToServer(token)
    }


    fun sendNotification(title: String?, body: String) {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP) // 액티비티 중복 생성 방지
        val pendingIntent = PendingIntent.getActivity(this, 0, intent,
            PendingIntent.FLAG_ONE_SHOT) // 일회성

        val channelId = getString(R.string.default_notification_channel_id)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION) // 소리
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setContentTitle(title) // 제목
            .setContentText(body) // 내용
            .setAutoCancel(true)
            .setSmallIcon(R.drawable.emotion2) // d알림영역에 노출될 아이콘
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // 오레오 버전 예외처리
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId,
                "Channel human readable title",
                NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }

        showDataMessage(title,body)
        val spf = getSharedPreferences("Alarm", MODE_PRIVATE) // 기존에 있던 데
        if(spf.getBoolean("state",true)){
            notificationManager.notify(0, notificationBuilder.build())
        }
    }

    private fun sendMessageNotification( Message : Map<String, String>){
        val uniId: Int = (System.currentTimeMillis() / 7).toInt()
        val title = Message["title"]!!
        val body = Message["body"]!!
        // PendingIntent : Intent 의 실행 권한을 외부의 어플리케이션에게 위임
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP) //\
        val pendingIntent = PendingIntent.getActivity(this, uniId, intent, PendingIntent.FLAG_ONE_SHOT)

        // 알림 채널 이름
        val channelId = getString(R.string.default_notification_channel_id)

        // 알림 소리
        val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        // 알림에 대한 UI 정보와 작업을 지정
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.emotion2)     // 아이콘 설정
            .setContentTitle(title)     // 제목
            .setContentText(body)     // 메시지 내용
            .setAutoCancel(true)
            .setSound(soundUri)     // 알림 소리
            .setContentIntent(pendingIntent)       // 알림 실행 시 Intent
            .setDefaults(Notification.DEFAULT_SOUND)


        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // 오레오 버전 이후에는 채널이 필요
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channel = NotificationChannel(channelId, "Notice", NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(channel)
            channel.apply {
                setShowBadge(false)
            }
        }
       // saveData(Message)
        // 알림 생성
        val spf = getSharedPreferences("Alarm", MODE_PRIVATE) // 기존에 있던 데
        if(spf.getBoolean("state",true)){
           notificationManager.notify(uniId, notificationBuilder.build())
        }

    }

    // 받은 토큰을 서버로 전송
    fun sendRegistrationToServer(token: String) {

    }


    fun showDataMessage(msgTitle: String?, msgContent: String?) {
        Log.i("### data msgTitle : ", msgTitle.toString())
        Log.i("### data msgContent : ", msgContent.toString())
        val toastText = String.format("[Data 메시지] title: %s => content: %s", msgTitle, msgContent)
        Looper.prepare()
        Toast.makeText(applicationContext, toastText, Toast.LENGTH_LONG).show()
        Looper.loop()
    }

    /**
     * 수신받은 메시지를 Toast로 보여줌
     * @param msgTitle
     * @param msgContent
     */
    fun showNotificationMessage(msgTitle: String?, msgContent: String?) {
        Log.i("### noti msgTitle : ", msgTitle.toString())
        Log.i("### noti msgContent : ", msgContent.toString())
        val toastText =
            String.format("[Notification 메시지] abcdedfge title: %s => content: %s", msgTitle, msgContent)
        Looper.prepare()
        Toast.makeText(applicationContext, toastText, Toast.LENGTH_LONG).show()
        Looper.loop()
    }

    fun saveData(Message:Map<String,String>){

        val title = Message["title"]!!
        val body = Message["body"]!!
        val emailID= Message["emailID"]!!
        val type= Message["type"]!!
        val timestamp= Message["timestamp"]!!
        val fromToken = Message["fromToken"]!!
        val fromUser = Message["fromUser"]!!


        val spf = getSharedPreferences("notification", MODE_PRIVATE) // 기존에 있던 데이터
        val editor =spf.edit()
        val gson = GsonBuilder().create()
        val data = MessageDTO(title, body,emailID,type,timestamp,fromToken,fromUser)
        val tempArray =ArrayList<MessageDTO>()
        val groupListType: Type = object : TypeToken<ArrayList<MessageDTO?>?>() {}.type

        val prev =spf.getString("messageList","none") // json list 가져오기
        val convertedData = gson.toJson(prev)

        Log.e(TAG, data.toString())
        if(prev!="none"){
            Log.e(TAG, "prev Json=> "+convertedData.toString())
            if(prev!="[]" || prev!="")tempArray.addAll(gson.fromJson(prev,groupListType))
            tempArray.add(data)
            val strList = gson.toJson(tempArray,groupListType)
            Log.e(TAG, "saved1:"+ strList)
            editor.putString("messageList",strList)
        }else{
            tempArray.add(data)
            val strList = gson.toJson(tempArray,groupListType)
            editor.putString("messageList",strList)
        }
        editor.apply()

    }


}
