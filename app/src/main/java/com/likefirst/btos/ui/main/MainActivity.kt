package com.likefirst.btos.ui.main


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.drawerlayout.widget.DrawerLayout.LOCK_MODE_UNLOCKED
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationBarView
import com.google.common.reflect.TypeToken
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.GsonBuilder
import com.likefirst.btos.R
import com.likefirst.btos.data.entities.firebase.MessageDTO
import com.likefirst.btos.data.entities.firebase.NotificationDTO
import com.likefirst.btos.data.local.FCMDatabase
import com.likefirst.btos.data.local.NotificationDatabase
import com.likefirst.btos.data.remote.notify.response.NoticeDetailResponse
import com.likefirst.btos.data.remote.notify.service.FCMService
import com.likefirst.btos.data.remote.notify.service.NoticeService
import com.likefirst.btos.data.remote.notify.view.NoticeAPIView
import com.likefirst.btos.data.remote.notify.view.SharedNotifyModel
import com.likefirst.btos.databinding.ActivityMainBinding
import com.likefirst.btos.ui.BaseActivity
import com.likefirst.btos.ui.archive.ArchiveFragment
import com.likefirst.btos.ui.history.HistoryFragment
import com.likefirst.btos.ui.home.HomeFragment
import com.likefirst.btos.ui.home.MailViewActivity
import com.likefirst.btos.ui.home.MailboxFragment
import com.likefirst.btos.ui.profile.ProfileFragment
import com.likefirst.btos.ui.profile.setting.NoticeActivity
import com.likefirst.btos.utils.toArrayList
import java.lang.reflect.Type
import kotlin.random.Random


class MainActivity: BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate),NoticeAPIView{

    var USERIDX=-1
    private var auth : FirebaseAuth? = null


    private val homeFragment = HomeFragment()
    private val archiveFragment = ArchiveFragment()
    private val historyFragment = HistoryFragment()
    private val profileFragment= ProfileFragment()

    var isDrawerOpen =true
    var isMailOpen=false

    lateinit var noticeList :ArrayList<NoticeDetailResponse>
    var prevNoticeSize : Int =0
    lateinit var  sharedNotifyModel: SharedNotifyModel

    interface onBackPressedListener {
        fun onBackPressed();
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        sharedNotifyModel= ViewModelProvider(this).get(SharedNotifyModel::class.java)

    }


   override fun initAfterBinding() {
        val notificationDatabase= NotificationDatabase.getInstance(this)!!
        prevNoticeSize = notificationDatabase.NotificationDao().itemCount()

        binding.mainBnv.itemIconTintList = null
        initNotice()

        binding.mainLayout.addDrawerListener(object:DrawerLayout.DrawerListener{
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {}
            override fun onDrawerOpened(drawerView: View) {
                val notifications =notificationDatabase.NotificationDao().getNotifications()
                if(notifications.isEmpty()){
                    initNotice()
                }else{
                    val loadData = loadFromFirebase(notifications.toArrayList())
                    initNotifyAdapter(loadData)
                    //메세지만 업데이트 한다
                }
            }
            override fun onDrawerClosed(drawerView: View) {}
            override fun onDrawerStateChanged(newState: Int) {

            }
        })

        supportFragmentManager.beginTransaction()
            .replace(R.id.fr_layout, homeFragment, "home")
            .setReorderingAllowed(true)
            .commitNowAllowingStateLoss()


        binding.mainBnv.setOnItemSelectedListener { it ->BottomNavView().onNavigationItemSelected(it) }
    }

    fun initNotice(){
        val NoticeService= NoticeService()
        NoticeService.setNoticeView(this)
        NoticeService.loadNotice()

    }



    inner class BottomNavView :NavigationBarView.OnItemSelectedListener {
        override fun onNavigationItemSelected(it: MenuItem): Boolean {

            when (it.itemId) {
                R.id.homeFragment -> {
                    isDrawerOpen=true
                    if (homeFragment.isAdded) {
                        supportFragmentManager.beginTransaction()
                            .hide(archiveFragment)
                            .hide(historyFragment)
                            .hide(profileFragment)
                            .show(homeFragment)
                            .setReorderingAllowed(true)
                            .commitNowAllowingStateLoss()
                        Log.d("homeclick", "added")
                    } else {
                        supportFragmentManager.beginTransaction()
                            .hide(archiveFragment)
                            .hide(profileFragment)
                            .hide(historyFragment)
                            .add(R.id.fr_layout, homeFragment, "home")
                            .show(homeFragment)
                            .setReorderingAllowed(true)
                            .commitAllowingStateLoss()
                        Log.d("homeClick", "noadded")
                    }
                    return true
                }

                R.id.historyFragment ->{
                    isDrawerOpen=false
                    val editor= getSharedPreferences("HistoryBackPos", AppCompatActivity.MODE_PRIVATE).edit()
                    editor.clear()
                    editor.commit()
                    if(historyFragment.isAdded){
                        supportFragmentManager.beginTransaction()
                            .hide(archiveFragment)
                            .hide(homeFragment)
                            .hide(profileFragment)
                            .show(historyFragment)
                            .setReorderingAllowed(true)
                            .commitNowAllowingStateLoss()
                        Log.d("historyClick", "added")
                    }else{
                        supportFragmentManager.beginTransaction()
                            .hide(homeFragment)
                            .hide(archiveFragment)
                            .hide(profileFragment)
                            .add(R.id.fr_layout, historyFragment, "history")
                            .show(historyFragment)
                            .setReorderingAllowed(true)
                            .commitAllowingStateLoss()
                        Log.d("historyClick", "noadded")
                    }
                    return true
                }

                R.id.archiveFragment -> {
//                    supportFragmentManager.beginTransaction()
//                        .replace(R.id.fr_layout, archiveFragment)
//                        .setReorderingAllowed(true)
//                        .commitNowAllowingStateLoss()
                    isDrawerOpen=false
                    if (archiveFragment.isAdded) {
                        supportFragmentManager.beginTransaction()
                            .hide(homeFragment)
                            .hide(historyFragment)
                            .hide(profileFragment)
                            .show(archiveFragment)
                            .setReorderingAllowed(true)
                            .commitNowAllowingStateLoss()
                        Log.d("archiveClick", "added")
                    } else {
                        supportFragmentManager.beginTransaction()
                            .hide(homeFragment)
                            .hide(profileFragment)
                            .hide(historyFragment)
                            .add(R.id.fr_layout, archiveFragment, "archive")
                            .show(archiveFragment)
                            .setReorderingAllowed(true)
                            .commitAllowingStateLoss()
                        Log.d("archiveClick", "noadded")
                    }
                    return true
                }
                R.id.profileFragment -> {
                    isDrawerOpen=false
                    if (profileFragment.isAdded) {
                        supportFragmentManager.beginTransaction()
                            .hide(homeFragment)
                            .hide(historyFragment)
                            .hide(archiveFragment)
                            .show(profileFragment)
                            .setReorderingAllowed(true)
                            .commitNowAllowingStateLoss()
                        Log.d("profileClick", "added")
                    } else {
                        supportFragmentManager.beginTransaction()
                            .hide(homeFragment)
                            .hide(archiveFragment)
                            .hide(historyFragment)
                            .add(R.id.fr_layout, profileFragment, "profile")
                            .show(profileFragment)
                            .setReorderingAllowed(true)
                            .commitAllowingStateLoss()
                        Log.d("profileClick", "noadded")
                    }
                    return true
                }
            }
            return false
        }
    }

    fun mailOpenStatus():Boolean{
        return isMailOpen
    }

    fun notifyDrawerHandler(Option : String){
        when(Option){
            "open"->{
                Log.d("Draw","open")
                binding.mainLayout.setDrawerLockMode(LOCK_MODE_UNLOCKED)
                binding.mainLayout.openDrawer((GravityCompat.START))

            }
            "unlock"->{
                Log.d("Draw","unlock")
                binding.mainLayout.setDrawerLockMode(LOCK_MODE_UNLOCKED)
            }
            "lock"->{
                Log.d("Draw","lock")
                binding.mainLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
            }
        }
    }


    override fun onBackPressed() {
        if(homeFragment.isVisible && !isMailOpen){
            finish()
        } else {

            val fragmentList = supportFragmentManager.fragments
            for (fragment in fragmentList) {
                if (fragment is onBackPressedListener) {
                    (fragment as onBackPressedListener).onBackPressed()
                    return
                }
            }

            if(homeFragment.isAdded){
                supportFragmentManager.beginTransaction()
                    .show(homeFragment)
                    .hide(archiveFragment)
                    .hide(profileFragment)
                    .hide(historyFragment)
                    .commitNow()
            } else {
                supportFragmentManager.beginTransaction()
                    .hide(archiveFragment)
                    .hide(profileFragment)
                    .hide(historyFragment)
                    .add(R.id.fr_layout, homeFragment)
                    .commitNow()
            }
            binding.mainBnv.menu.findItem(R.id.homeFragment).isChecked = true
        }
    }

    fun initNotifyAdapter(notificationList:  ArrayList<NotificationDTO>){
        Log.e("Firebase -> Adapter ", "Result: ${notificationList}")
        val adapter = NotifyRVAdapter(notificationList)
         adapter.setMyItemCLickLister(object : NotifyRVAdapter.NotifyItemClickListener {
            override fun onClickItem(item : NotificationDTO) {
                binding.mainLayout.closeDrawers()
                when(item.type){
                    "notice"-> {
                        startNextActivity(NoticeActivity::class.java)
                    }
                    "letter"->{
                        //body date sender 구현 필수
                        val bundle = bundleOf("body" to item.content, "date" to item.timestamp , "sender" to "sample")
                        val intent = Intent(this@MainActivity,MailViewActivity::class.java)
                        intent.putExtra("MailView",bundle)
                        startActivity(intent)
                    }
                    "diary"->{
                        //TODO 구현
                    }
                }

            }
        })
        binding.sidebarNotifyRv.adapter = adapter
    }

    override fun onNoticeAPIError(Dialog: CustomDialogFragment) {
        Dialog.show(supportFragmentManager,"noticeErrorDialog")
    }

    override fun onNoticeAPISuccess(noticeData: ArrayList<NoticeDetailResponse>) {
        noticeList=noticeData
        var notificationList =ArrayList<NotificationDTO>()
        noticeData.map{
                notice ->notificationList.add(NotificationDTO(notice.createdAt,
            "BTOS_SERVER","notice",notice.noticeIdx,notice.title,notice.content,"BTOS_SERVER")
        )}
        val result = loadFromFirebase(notificationList)
        Log.e("NOTICE/API -> Firebase", "Result: ${result}")
        initNotifyAdapter( result )
        //TODO 공지 DB 비교해서 아이콘 표시 구현
    }

    override fun onNoticeAPIFailure(code: Int, message: String) {
        when(code){
            4000->Log.e(code.toString(), "데이터베이스 연결에 실패하였습니다.")
            else -> Log.e(code.toString(), "공지 조회 실패.")
        }
    }

    fun rand(): Int {
        val rand = Random(System.nanoTime())
        return (0..100000).random(rand)
    }

    fun loadFromFirebase(noticeList : ArrayList<NotificationDTO>):ArrayList<NotificationDTO> {
        val gson = GsonBuilder().create()
        var notifications  = noticeList
        val spf = getSharedPreferences("notification", MODE_PRIVATE) // 기존에 있던 데이터
        val notification = spf.getString("messageList", "undefine")
        val groupListType: Type = object : TypeToken<ArrayList<MessageDTO?>?>() {}.type
        if (notification =="undefine") {
            Toast.makeText(this, "메세지 로드에 실패했습니다", Toast.LENGTH_SHORT)
            return notifications
        }
        val list: ArrayList<MessageDTO> = gson.fromJson(notification, groupListType)
        Log.e("Firebase/list", list.toString())
        if(list.size ==0){
            val bundle = bundleOf("mail" to false)
            sharedNotifyModel.setLiveData(bundle)
            return notifications
        }


        list.map{
            i->notifications.add(NotificationDTO(i.timestamp!!,i.fromToken, i.type!! , rand(),i.title,i.body,i.fromUser))
        }
        //TODO rand()-> 각 공지, 편지, 다이어리의 idx 로 수정
        val notificationDatabase= NotificationDatabase.getInstance(this)!!
        if(notificationDatabase.NotificationDao().getNotifications().isEmpty()){
            notifications.forEach {
                notificationDatabase.NotificationDao().insert(it)
            }
        }else{
            notifications.forEach {
                if(notificationDatabase.NotificationDao().getNotification(it.timestamp, it.detailIdx, it.type) ==null)
                    notificationDatabase.NotificationDao().insert(it)
            }
        }

        val bundle = bundleOf("notification" to true, "mail" to true)
        sharedNotifyModel.setLiveData(bundle)

        Log.e("Firebaese/DB",notificationDatabase.NotificationDao().getNotifications().toString() )
        val editor = spf.edit()
        editor.remove("messageList")
        editor.apply()  //저장된 건 삭제하기

        return notificationDatabase.NotificationDao().getNotifications().toArrayList()
    }

    override fun onRestart() {
        super.onRestart()
        binding.mainLayout.setDrawerLockMode(LOCK_MODE_UNLOCKED)
    }
}