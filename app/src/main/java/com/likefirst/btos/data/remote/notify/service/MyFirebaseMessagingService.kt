package com.likefirst.btos.data.remote.notify.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.graphics.drawable.IconCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.likefirst.btos.R
import com.likefirst.btos.ui.main.MainActivity
import android.os.Looper
import android.util.Log

import android.widget.Toast
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.likefirst.btos.utils.fcm.MyWorker


class MyFirebaseMessagingService : FirebaseMessagingService() {
    val TAG = "FirebaseTest"

    // 메세지가 수신되면 호출
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.i("### msg : ", remoteMessage.toString());
        if (remoteMessage.data.isEmpty()) {
            showNotificationMessage(remoteMessage.notification?.title, remoteMessage.notification?.body);  // Notification으로 받을 때
        } else {
            showDataMessage(remoteMessage.data.get("title"), remoteMessage.data.get("content"));  // Data로 받을 때
        }

        // 서버에서 직접 보냈을 때
        if(remoteMessage.notification != null){
            sendNotification(remoteMessage.notification?.title,
                remoteMessage.notification?.body!!)
        }

        // 다른 기기에서 서버로 보냈을 때
        else if(remoteMessage.data.isNotEmpty()){
            val title = remoteMessage.data["title"]!!
            val userId = remoteMessage.data["userId"]!!
            val message = remoteMessage.data["message"]!!

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                sendMessageNotification(title,userId,message)
            }
            else{
                sendNotification(remoteMessage.notification?.title,
                    remoteMessage.notification?.body!!)
            }
        }
    }

    // Firebase Cloud Messaging Server 가 대기중인 메세지를 삭제 시 호출
    override fun onDeletedMessages() {
        super.onDeletedMessages()
    }

    // 메세지가 서버로 전송 성공 했을때 호출
    override fun onMessageSent(p0: String) {
        super.onMessageSent(p0)
    }

    // 메세지가 서버로 전송 실패 했을때 호출
    override fun onSendError(p0: String, p1: Exception) {
        super.onSendError(p0, p1)
    }

    // 새로운 토큰이 생성 될 때 호출
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d(TAG,"Refreshed token : $token")
        sendRegistrationToServer(token)
    }

    private fun scheduleJob(){
        //[START dispatch job]
        val work = OneTimeWorkRequest.Builder(MyWorker::class.java).build()
        WorkManager.getInstance(this).beginWith(work).enqueue()
        //[END dispatch)job]
    }

    fun sendNotification(title: String?, body: String) {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP) // 액티비티 중복 생성 방지
        val pendingIntent = PendingIntent.getActivity(this, 0, intent,
            PendingIntent.FLAG_ONE_SHOT) // 일회성

        val channelId = "channel" // 채널 아이디
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION) // 소리
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setContentTitle(title) // 제목
            .setContentText(body) // 내용
            .setAutoCancel(true)
            .setSmallIcon(R.drawable.emotion1) // d알림영역에 노출될 아이콘
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

        notificationManager.notify(0, notificationBuilder.build()) // 알림 생성
    }

    @RequiresApi(Build.VERSION_CODES.P)
    private fun sendMessageNotification(title: String,userId: String, body: String){
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP) // 액티비티 중복 생성 방지
        val pendingIntent = PendingIntent.getActivity(this, 0 , intent,
            PendingIntent.FLAG_ONE_SHOT) // 일회성

        // messageStyle 로
        val user: androidx.core.app.Person = androidx.core.app.Person.Builder()
            .setName(userId)
            .setIcon(IconCompat.createWithResource(this, R.drawable.emotion1))
            .build()

        val message = NotificationCompat.MessagingStyle.Message(
            body,
            System.currentTimeMillis(),
            user
        )
        val messageStyle = NotificationCompat.MessagingStyle(user)
            .addMessage(message)


        val channelId = "channel" // 채널 아이디
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION) // 소리
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setContentTitle(title) // 제목
            .setContentText(body) // 내용
            .setStyle(messageStyle)
            .setSmallIcon(R.drawable.emotion2) // 아이콘
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // 오레오 버전 예외처리
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId,
                "알림 메세지",
                NotificationManager.IMPORTANCE_LOW) // 소리없앰
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(0 , notificationBuilder.build()) // 알림 생성
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
            String.format("[Notification 메시지] title: %s => content: %s", msgTitle, msgContent)
        Looper.prepare()
        Toast.makeText(applicationContext, toastText, Toast.LENGTH_LONG).show()
        Looper.loop()
    }
}
