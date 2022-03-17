package com.likefirst.btos.data.remote.notify.service

import android.app.*
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.likefirst.btos.presentation.view.main.MainActivity
import android.util.Log
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.likefirst.btos.BuildConfig
import com.likefirst.btos.R
import com.likefirst.btos.utils.fcm.MyWorker
import com.likefirst.btos.data.remote.notify.view.FcmTokenView
import com.likefirst.btos.data.remote.notify.view.NoticeAPIView
import com.likefirst.btos.data.remote.plant.viewmodel.PlantViewModel
import com.likefirst.btos.utils.getAlarmSound
import com.likefirst.btos.utils.getUserIdx
import com.likefirst.btos.utils.saveNotification


class MyFirebaseMessagingService : FirebaseMessagingService(),FcmTokenView {
    val TAG = "MSG_Firebase"
    lateinit var listener : NoticeAPIView

    // 메세지가 수신되면 호출
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.i(TAG, remoteMessage.toString());

        // 서버에서 직접 보냈을 때
        if(remoteMessage.notification != null){
            sendNotification(remoteMessage.notification?.title, remoteMessage.notification?.body!!)
            if (true) {
                scheduleJob();
            } else {
                handleNow();
            }
        }

        // 다른 기기에서 서버로 보냈을 때
        else if(remoteMessage.data.isNotEmpty()){
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
        Log.e(TAG, "sending success ")
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


    fun sendNotification(title: String?, body: String) {
        Log.e(TAG,"sendNotification")
        val uniId: Int = (System.currentTimeMillis() / 7).toInt()
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP) // 액티비티 중복 생성 방지
        val pendingIntent = PendingIntent.getActivity(this, 0, intent,
            PendingIntent.FLAG_ONE_SHOT) // 일회성

        val channelId = BuildConfig.DEFAULT_NOTIFICATION_CHANNEL_ID
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION) // 소리

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.mipmap.ic_launcher)     // 아이콘 설정
            .setContentTitle(title)     // 제목
            .setContentText(body)     // 메시지 내용
            .setAutoCancel(true)
            .setSound(defaultSoundUri)     // 알림 소리
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
        filterNotification(title!!,body)
        if(getAlarmSound()){
            notificationManager.notify(uniId, notificationBuilder.build())
        }
        Log.e(TAG,"title: ${title} body: ${body}")


    }

    private fun sendMessageNotification( Message : Map<String, String>){
        Log.e(TAG,"sendMessageNotification")
        val uniId: Int = (System.currentTimeMillis() / 7).toInt()
        val title = Message["title"]!!
        val body = Message["body"]!!
        // PendingIntent : Intent 의 실행 권한을 외부의 어플리케이션에게 위임
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, uniId, intent, PendingIntent.FLAG_ONE_SHOT)

        // 알림 채널 이름
        val channelId = BuildConfig.DEFAULT_NOTIFICATION_CHANNEL_ID

        // 알림 소리
        val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        // 알림에 대한 UI 정보와 작업을 지정
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.mipmap.ic_launcher)     // 아이콘 설정
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
        filterNotification(title,body)
        //TODO 알림 추후
        if(getAlarmSound()){
           notificationManager.notify(uniId, notificationBuilder.build())
        }
        Log.e(TAG,"title: ${title} body: ${body}")

    }

    // 받은 토큰을 서버로 전송
    fun sendRegistrationToServer(token: String) {
        if(getUserIdx()!=0) {
            val fcmService= FcmTokenService()
            fcmService.setFcmTokenView(this)
            fcmService.postFcmToken(getUserIdx(),token)
        }
    }


    fun filterNotification(title: String , content: String){
        if("식물" in title || "화분" in title){
            val plantViewModel = PlantViewModel(application)
            val plant = plantViewModel.getSelectedPlant()
            val newLevel = Character.getNumericValue(title[title.indexOf("단계")-1])
            if(newLevel>=0)plantViewModel.setInitPlant(plant.plantIdx,plant.plantStatus,newLevel.toInt(),plant.isOwn)
            saveNotification("plant")
            return
        }else if("신고" in title){

        } else if(title.contains("공지사항")){
            saveNotification("notice")
        }// 편지, 일기 처리
        else{
            saveNotification("mail")
        }
    }


    override fun onLoadingFcmToken() {}

    override fun onSuccessFcmToken() {}

    override fun onFailureFcmToken(code: Int, msg: String) {}


}
